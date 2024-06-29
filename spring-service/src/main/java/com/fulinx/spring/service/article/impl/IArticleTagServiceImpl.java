/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.article.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.dao.mapper.IArticleTagDao;
import com.fulinx.spring.data.mysql.dao.podo.article.ArticleTagListResultDo;
import com.fulinx.spring.data.mysql.entity.TbArticleTagEntity;
import com.fulinx.spring.data.mysql.service.TbArticleTagEntityService;
import com.fulinx.spring.service.article.*;
import com.fulinx.spring.service.article.dto.ArticleTagListResultDto;
import com.fulinx.spring.service.article.dto.ArticleTagQueryConditionDto;
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
public class IArticleTagServiceImpl implements IArticleTagService {

    private final IArticleTagDao iArticleTagDao;

    private final IArticleService iArticleService;

    private final TbArticleTagEntityService tbArticleTagEntityService;

    @Lazy
    @Autowired
    public IArticleTagServiceImpl(IArticleTagDao iArticleTagDao, IArticleService iArticleService, TbArticleTagEntityService tbArticleTagEntityService) {
        this.iArticleTagDao = iArticleTagDao;
        this.iArticleService = iArticleService;
        this.tbArticleTagEntityService = tbArticleTagEntityService;
    }


    /**
     * @param articleId
     * @param tag
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Optional<TbArticleTagEntity> create(Integer articleId, String tag) throws BusinessException {
        // 检查文章是否存在
        iArticleService.lockById(articleId).orElseThrow(() -> {
            log.debug("新增文章SEO失败，失败原因，文章不存在，articleId = {}", articleId);
            return new BusinessException(ErrorMessageEnum.ARTICLE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_NOT_EXISTS.getIndex());
        });
        TbArticleTagEntity tbArticleTagEntity = new TbArticleTagEntity();
        tbArticleTagEntity.setArticleId(articleId);
        tbArticleTagEntity.setTagName(tag);
        boolean isOk = tbArticleTagEntityService.save(tbArticleTagEntity);
        return Optional.ofNullable(isOk ? tbArticleTagEntity : null);
    }

    /**
     * @param ids
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean remove(List<Integer> ids) throws BusinessException {
        for (Integer id : ids) {
            TbArticleTagEntity tbArticleEntity = this.lockById(id).orElseThrow(() -> {
                log.warn("删除分类失败，分类不存在，id = {}", id);
                return new BusinessException(ErrorMessageEnum.ARTICLE_TAG_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_TAG_NOT_EXISTS.getIndex());
            });
            tbArticleTagEntityService.removeById(tbArticleEntity);
        }
        return null;
    }


    /**
     * 锁表查询单条记录
     *
     * @param id
     * @return
     */
    @Override
    public Optional<TbArticleTagEntity> lockById(Serializable id) {
        return Optional.ofNullable(iArticleTagDao.lockById(id));
    }

    /**
     * 获取单条记录
     *
     * @param id
     * @return
     */
    @Override
//    @Cacheable(value = "article", key = "#id")
    public Optional<ArticleTagListResultDto> getById(Integer id) throws BusinessException {
        this.lockById(id).orElseThrow(() -> {
            log.warn("查看分类失败，分类不存在，id = {}", id);
            return new BusinessException(ErrorMessageEnum.ARTICLE_TAG_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_TAG_NOT_EXISTS.getIndex());
        });
        ArticleTagQueryConditionDto articleTagQueryConditionDto = new ArticleTagQueryConditionDto();
        articleTagQueryConditionDto.setId(id);
        List<ArticleTagListResultDto> list = this.list(articleTagQueryConditionDto);
        return Optional.ofNullable(list.size() > 0 ? list.get(0) : null);
    }

    /**
     * 权限列表查询
     *
     * @param articleTagQueryConditionDto
     * @return
     */
    @Override
    public List<ArticleTagListResultDto> list(ArticleTagQueryConditionDto articleTagQueryConditionDto) {
        ArticleTagQueryConditionDto articleTagListConditionPo = MiscUtils.copyProperties(articleTagQueryConditionDto, ArticleTagQueryConditionDto.class);
        List<ArticleTagListResultDo> list = iArticleTagDao.list(articleTagListConditionPo);
        List<ArticleTagListResultDo> articleTagListResultDos = MiscUtils.copyList(list, ArticleTagListResultDo.class);
        return MiscUtils.copyList(articleTagListResultDos, ArticleTagListResultDto.class);
    }

    /**
     * 权限列表带分页查询
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public IPage<ArticleTagListResultDto> page(ArticleTagQueryConditionDto articleTagQueryConditionDto, int pageNumber, int pageSize) {
        Page<ArticleTagQueryConditionDto> page = PageHelper.startPage(pageNumber, pageSize);
//        PageHelper.orderBy(String.format("%s desc", TbArticleEntity.ID));
        List<ArticleTagListResultDto> articleTagListResultDtoList = list(articleTagQueryConditionDto);
        IPage<ArticleTagListResultDto> articleTagListResultDtoIPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        articleTagListResultDtoIPage.setTotal(page.getTotal());
        articleTagListResultDtoIPage.setRecords(articleTagListResultDtoList);
        return articleTagListResultDtoIPage;
    }
}
