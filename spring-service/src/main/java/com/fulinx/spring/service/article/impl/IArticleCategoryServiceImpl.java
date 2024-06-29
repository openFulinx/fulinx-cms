/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.article.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.data.enums.RecordRemoveStatusEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.dao.mapper.IArticleCategoryDao;
import com.fulinx.spring.data.mysql.dao.podo.article.ArticleCategoryListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.article.ArticleCategoryListResultDo;
import com.fulinx.spring.data.mysql.entity.TbArticleCategoryRelationEntity;
import com.fulinx.spring.data.mysql.entity.TbCategoryEntity;
import com.fulinx.spring.data.mysql.service.TbArticleCategoryRelationEntityService;
import com.fulinx.spring.service.article.IArticleCategoryService;
import com.fulinx.spring.service.article.IArticleService;
import com.fulinx.spring.service.article.dto.ArticleCategoryListResultDto;
import com.fulinx.spring.service.article.dto.ArticleCategoryQueryConditionDto;
import com.fulinx.spring.service.category.ICategoryService;
import com.fulinx.spring.service.enums.ErrorMessageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IArticleCategoryServiceImpl implements IArticleCategoryService {

    private final TbArticleCategoryRelationEntityService tbArticleCategoryRelationEntityService;

    private final IArticleService iArticleService;

    private final ICategoryService iCategoryService;

    private final IArticleCategoryDao iArticleCategoryDao;

    @Lazy
    @Autowired
    public IArticleCategoryServiceImpl(TbArticleCategoryRelationEntityService tbArticleCategoryRelationEntityService, IArticleService iArticleService, ICategoryService iCategoryService, IArticleCategoryDao iArticleCategoryDao) {
        this.tbArticleCategoryRelationEntityService = tbArticleCategoryRelationEntityService;
        this.iArticleService = iArticleService;
        this.iCategoryService = iCategoryService;
        this.iArticleCategoryDao = iArticleCategoryDao;
    }


    /**
     * @param articleId
     * @param categoryId
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Optional<TbArticleCategoryRelationEntity> create(Integer articleId, Integer categoryId) throws BusinessException {
        // 检查文章是否存在
        iArticleService.lockById(articleId).orElseThrow(() -> {
            log.debug("新增文章分类失败，失败原因，文章不存在，articleId = {}", articleId);
            return new BusinessException(ErrorMessageEnum.ARTICLE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_NOT_EXISTS.getIndex());
        });

        // 检查分类是否存在
        iCategoryService.lockById(categoryId).orElseThrow(() -> {
            log.debug("新增文章分类失败，失败原因，分类不存在，categoryId = {}", categoryId);
            return new BusinessException(ErrorMessageEnum.CATEGORY_NOT_EXISTS.getMessage(), ErrorMessageEnum.CATEGORY_NOT_EXISTS.getIndex());
        });

        TbArticleCategoryRelationEntity tbArticleCategoryRelationEntity = new TbArticleCategoryRelationEntity();
        tbArticleCategoryRelationEntity.setArticleId(articleId);
        tbArticleCategoryRelationEntity.setCategoryId(categoryId);
        boolean isOk = tbArticleCategoryRelationEntityService.save(tbArticleCategoryRelationEntity);
        return Optional.ofNullable(isOk ? tbArticleCategoryRelationEntity : null);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean remove(List<Integer> ids) throws BusinessException {
        for (Integer id : ids) {
            TbArticleCategoryRelationEntity tbArticleCategoryRelationEntity = this.lockById(id).orElseThrow(() -> {
                log.debug("删除文章分类失败，失败原因，文章分类不存在，id = {}", id);
                return new BusinessException(ErrorMessageEnum.ARTICLE_CATEGORY_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_CATEGORY_NOT_EXISTS.getIndex());
            });
            tbArticleCategoryRelationEntity.setIsDelete(RecordRemoveStatusEnum._LOGICALLY_DELETED.getIndex());
            tbArticleCategoryRelationEntityService.removeById(tbArticleCategoryRelationEntity);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean update(Integer articleId, Integer categoryId) throws BusinessException {
        // 检查文章是否存在
        iArticleService.lockById(articleId).orElseThrow(() -> {
            log.debug("更新文章分类失败，失败原因，文章不存在，articleId = {}", articleId);
            return new BusinessException(ErrorMessageEnum.ARTICLE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_NOT_EXISTS.getIndex());
        });

        // 检查分类是否存在
        iCategoryService.lockById(categoryId).orElseThrow(() -> {
            log.debug("更新文章分类失败，失败原因，分类不存在，categoryId = {}", categoryId);
            return new BusinessException(ErrorMessageEnum.CATEGORY_NOT_EXISTS.getMessage(), ErrorMessageEnum.CATEGORY_NOT_EXISTS.getIndex());
        });
        List<TbArticleCategoryRelationEntity> list = tbArticleCategoryRelationEntityService.lambdaQuery().eq(TbArticleCategoryRelationEntity::getArticleId, articleId).eq(TbArticleCategoryRelationEntity::getCategoryId, categoryId).list();
        Boolean result = false;
        if (list.size() == 1) {
            TbArticleCategoryRelationEntity tbArticleCategoryRelationEntity = list.get(0);
            tbArticleCategoryRelationEntity.setArticleId(articleId);
            tbArticleCategoryRelationEntity.setCategoryId(categoryId);
            result = tbArticleCategoryRelationEntityService.updateById(tbArticleCategoryRelationEntity);
        }
        return result;
    }

    /**
     * 锁表查询单条记录
     *
     * @param id
     * @return
     */
    @Override
    public Optional<TbArticleCategoryRelationEntity> lockById(Serializable id) {
        return Optional.ofNullable(iArticleCategoryDao.lockById(id));
    }

    /**
     * 获取单条记录
     *
     * @param id
     * @return
     */
    @Override
    public Optional<ArticleCategoryListResultDto> getById(Integer id) throws BusinessException {
        this.lockById(id).orElseThrow(() -> {
            log.warn("查看分类失败，分类不存在，id = {}", id);
            return new BusinessException(ErrorMessageEnum.ARTICLE_CATEGORY_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_CATEGORY_NOT_EXISTS.getIndex());
        });
        ArticleCategoryQueryConditionDto articleCategoryQueryConditionDto = new ArticleCategoryQueryConditionDto();
        articleCategoryQueryConditionDto.setId(id);
        List<ArticleCategoryListResultDto> list = this.list(articleCategoryQueryConditionDto);
        return Optional.ofNullable(list.size() > 0 ? list.get(0) : null);
    }

    /**
     * 列表查询
     *
     * @param articleCategoryQueryConditionDto
     * @return
     */
    @Override
    public List<ArticleCategoryListResultDto> list(ArticleCategoryQueryConditionDto articleCategoryQueryConditionDto) {
        ArticleCategoryListConditionPo articleCategoryListConditionPo = MiscUtils.copyProperties(articleCategoryQueryConditionDto, ArticleCategoryListConditionPo.class);
        List<ArticleCategoryListResultDo> list = iArticleCategoryDao.list(articleCategoryListConditionPo);
        return MiscUtils.copyList(list, ArticleCategoryListResultDto.class);
    }

    /**
     * 列表带分页查询
     *
     * @param articleCategoryQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public IPage<ArticleCategoryListResultDto> page(ArticleCategoryQueryConditionDto articleCategoryQueryConditionDto, int pageNumber, int pageSize) {
        Page<ArticleCategoryListResultDo> page = PageHelper.startPage(pageNumber, pageSize);
        PageHelper.orderBy(String.format("%s desc", TbCategoryEntity.ID));
        List<ArticleCategoryListResultDto> articleCategoryListResultDtoList = list(articleCategoryQueryConditionDto);
        IPage<ArticleCategoryListResultDto> articleCategoryListResultDtoIPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        articleCategoryListResultDtoIPage.setTotal(page.getTotal());
        articleCategoryListResultDtoIPage.setRecords(articleCategoryListResultDtoList);
        return articleCategoryListResultDtoIPage;
    }
}
