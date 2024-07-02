/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.article.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.data.enums.RecordRemoveStatusEnum;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.dao.mapper.IArticleDao;
import com.fulinx.spring.data.mysql.dao.podo.article.ArticleListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.article.ArticleListResultDo;
import com.fulinx.spring.data.mysql.entity.*;
import com.fulinx.spring.data.mysql.enums.SimpleStatusEnum;
import com.fulinx.spring.data.mysql.service.*;
import com.fulinx.spring.service.article.*;
import com.fulinx.spring.service.article.dto.ArticleListResultDto;
import com.fulinx.spring.service.article.dto.ArticleQueryConditionDto;
import com.fulinx.spring.service.category.ICategoryService;
import com.fulinx.spring.service.enums.ErrorMessageEnum;
import com.fulinx.spring.service.file.IFileService;
import com.fulinx.spring.service.user.IUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

@Slf4j
@Service
public class IArticleServiceImpl implements IArticleService {
    private final TbArticleEntityService tbArticleEntityService;

    private final TbArticleTagEntityService tbArticleTagEntityService;

    private final TbArticleFileRelationEntityService tbArticleFileRelationEntityService;

    private final IArticleDao iArticleDao;

    private final IArticleSeoService iArticleSeoService;

    private final IArticleFileService iArticleFileService;

    private final IArticleCategoryService iArticleCategoryService;

    private final IArticleTagService iArticleTagService;

    private final IFileService iFileService;

    private final ICategoryService iCategoryService;

    private final TbFileEntityService tbFileEntityService;

    private final IUserService iUserService;

    private final TbUserEntityService tbUserEntityService;

    private final TbUserProfileEntityService tbUserProfileEntityService;

    private final TbArticleCategoryRelationEntityService tbArticleCategoryRelationEntityService;

    private final TbCategoryEntityService tbCategoryEntityService;

    private final TbArticleSeoEntityService tbArticleSeoEntityService;

    private final IArticleDetailService iArticleDetailService;

    private final TbArticleDetailEntityService tbArticleDetailEntityService;

    @Autowired
    @Lazy
    public IArticleServiceImpl(TbArticleEntityService tbArticleEntityService, TbArticleTagEntityService tbArticleTagEntityService, TbArticleFileRelationEntityService tbArticleFileRelationEntityService, IArticleDao iArticleDao, IArticleSeoService iArticleSeoService, IArticleFileService iArticleFileService, IArticleCategoryService iArticleCategoryService, IArticleTagService iArticleTagService, IFileService iFileService, ICategoryService iCategoryService, TbFileEntityService tbFileEntityService, IUserService iUserService, TbUserEntityService tbUserEntityService, TbUserProfileEntityService tbUserProfileEntityService, TbArticleCategoryRelationEntityService tbArticleCategoryRelationEntityService, TbCategoryEntityService tbCategoryEntityService, TbArticleSeoEntityService tbArticleSeoEntityService, IArticleDetailService iArticleDetailService, TbArticleDetailEntityService tbArticleDetailEntityService) {
        this.tbArticleEntityService = tbArticleEntityService;
        this.tbArticleTagEntityService = tbArticleTagEntityService;
        this.tbArticleFileRelationEntityService = tbArticleFileRelationEntityService;
        this.iArticleDao = iArticleDao;
        this.iArticleSeoService = iArticleSeoService;
        this.iArticleFileService = iArticleFileService;
        this.iArticleCategoryService = iArticleCategoryService;
        this.iArticleTagService = iArticleTagService;
        this.iFileService = iFileService;
        this.iCategoryService = iCategoryService;
        this.tbFileEntityService = tbFileEntityService;
        this.iUserService = iUserService;
        this.tbUserEntityService = tbUserEntityService;
        this.tbUserProfileEntityService = tbUserProfileEntityService;
        this.tbArticleCategoryRelationEntityService = tbArticleCategoryRelationEntityService;
        this.tbCategoryEntityService = tbCategoryEntityService;
        this.tbArticleSeoEntityService = tbArticleSeoEntityService;
        this.iArticleDetailService = iArticleDetailService;
        this.tbArticleDetailEntityService = tbArticleDetailEntityService;
    }


    /**
     * 后台用户新增文章
     *
     * @param articleName
     * @param articleDescription
     * @param metaTitle
     * @param metaDescription
     * @param fileIds
     * @param status
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Optional<TbArticleEntity> create(Integer articleType, Integer languageId, String articleName, String articleDescription, String customs, String metaTitle, String metaDescription, List<Integer> fileIds, List<Integer> categoryIds, List<String> tags, Boolean status) throws BusinessException {

        // 检查File是否存在
        for (Integer fileId : fileIds) {
            iFileService.lockById(fileId).orElseThrow(() -> {
                log.debug("新增文章失败，失败原因，文件不存在，fileId = {}", fileId);
                return new BusinessException(ErrorMessageEnum.FILE_NOT_EXISTS.getMessage(), ErrorMessageEnum.FILE_NOT_EXISTS.getIndex());
            });
        }

        if (categoryIds.size() > 0) {
            for (Integer categoryId : categoryIds) {
                // 检查分类是否存在
                iCategoryService.lockById(categoryId).orElseThrow(() -> {
                    log.debug("新增文章失败，失败原因，分类不存在，categoryId = {}", categoryId);
                    return new BusinessException(ErrorMessageEnum.CATEGORY_NOT_EXISTS.getMessage(), ErrorMessageEnum.CATEGORY_NOT_EXISTS.getIndex());
                });
            }
        }

        if (!SimpleStatusEnum.contains(status == true ? 1 : 0)) {
            log.debug("新增文章失败，失败原因，状态不存在，status = {}", status);
            throw new BusinessException(ErrorMessageEnum.ARTICLE_STATUS_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_STATUS_NOT_EXISTS.getIndex());
        }

        TbArticleEntity tbArticleEntity = new TbArticleEntity();
        tbArticleEntity.setArticleType(articleType);
        tbArticleEntity.setStatus(status);
        boolean isOk = tbArticleEntityService.save(tbArticleEntity);
        if (isOk) {
            iArticleDetailService.create(tbArticleEntity.getId(), languageId, articleName, articleDescription, customs);
            iArticleSeoService.create(tbArticleEntity.getId(), languageId, metaTitle, metaDescription);
            for (Integer fileId : fileIds) {
                iArticleFileService.create(tbArticleEntity.getId(), fileId);
            }
            if (categoryIds.size() > 0) {
                for (Integer categoryId : categoryIds) {
                    iArticleCategoryService.create(tbArticleEntity.getId(), categoryId);
                }
            }
            if (tags.size() > 0) {
                for (String tag : tags) {
                    iArticleTagService.create(tbArticleEntity.getId(), tag);
                }
            }
        }

        return Optional.ofNullable(isOk ? tbArticleEntity : null);
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
            TbArticleEntity tbArticleEntity = this.lockById(id).orElseThrow(() -> {
                log.warn("删除文章失败，文章不存在，id = {}", id);
                return new BusinessException(ErrorMessageEnum.ARTICLE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_NOT_EXISTS.getIndex());
            });
            tbArticleEntityService.removeById(tbArticleEntity);
//            // 删除SEO
//            iArticleSeoService.removeByArticleId(id);
//            // 删除文章详情
//            iArticleDetailService.removeByArticleId(id);
//            // 删除文章文件
//            iArticleFileService.removeByArticleId(id);
//            // 删除文章分类
//            iArticleCategoryService.remove(id);
//            // 删除文章标签
//            iArticleTagService.remove(id);
        }
        return null;
    }

    /**
     * 后台用户修改文章
     *
     * @param id
     * @param articleName
     * @param articleDescription
     * @param metaTitle
     * @param metaDescription
     * @param fileIds
     * @param deletedFileIds
     * @param categoryIds
     * @param tags
     * @param deletedTagIds
     * @param status
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public TbArticleEntity update(Integer articleType,Integer id, Integer languageId, String articleName, String articleDescription, String customs, String metaTitle, String metaDescription, List<Integer> fileIds, List<Integer> deletedFileIds, List<Integer> categoryIds, List<Integer> deletedCategoryIds, List<String> tags, List<Integer> deletedTagIds, Boolean status) throws BusinessException {
        TbArticleEntity tbArticleEntity = this.lockById(id).orElseThrow(() -> {
            log.warn("更新文章失败，文章不存在，id = {}", id);
            return new BusinessException(ErrorMessageEnum.ARTICLE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_NOT_EXISTS.getIndex());
        });

        // 检查File是否存在, 筛选出目前数据库没有的articleFileId
        HashSet<Integer> notSaveFileIds = new HashSet<>();
        if (!fileIds.isEmpty()) {
            for (Integer fileId : fileIds) {
                List<TbArticleFileRelationEntity> tbArticleFileRelationEntityList = tbArticleFileRelationEntityService.lambdaQuery().eq(TbArticleFileRelationEntity::getFileId, fileId).eq(TbArticleFileRelationEntity::getArticleId, id).list();
                if (tbArticleFileRelationEntityList.size() == 0) {
                    iFileService.lockById(fileId).orElseThrow(() -> {
                        log.debug("更新文章失败，失败原因，文件不存在，fileId = {}", fileId);
                        return new BusinessException(ErrorMessageEnum.FILE_NOT_EXISTS.getMessage(), ErrorMessageEnum.FILE_NOT_EXISTS.getIndex());
                    });
                    notSaveFileIds.add(fileId);
                }
            }
        }

        if (deletedFileIds != null && deletedFileIds.size() > 0) {
            // 检查File是否存在
            for (Integer deletedArticleFileId : deletedFileIds) {
                List<TbArticleFileRelationEntity> tbArticleFileRelationEntityList = tbArticleFileRelationEntityService.lambdaQuery().eq(TbArticleFileRelationEntity::getFileId, deletedArticleFileId).eq(TbArticleFileRelationEntity::getArticleId, id).list();
                TbArticleFileRelationEntity tbArticleFileRelationEntity = iArticleFileService.lockById(tbArticleFileRelationEntityList.get(0).getId()).orElseThrow(() -> {
                    log.debug("更新文章失败，失败原因，文章文件不存在，articleFieldId = {}", deletedArticleFileId);
                    return new BusinessException(ErrorMessageEnum.ARTICLE_FILE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_FILE_NOT_EXISTS.getIndex());
                });
                tbArticleFileRelationEntityService.removeById(tbArticleFileRelationEntity);
            }
        }

        if (deletedCategoryIds.size() > 0) {
            for (Integer deletedCategoryId : deletedCategoryIds) {
                List<TbArticleCategoryRelationEntity> list = tbArticleCategoryRelationEntityService.lambdaQuery()
                        .eq(TbArticleCategoryRelationEntity::getCategoryId, deletedCategoryId)
                        .eq(TbArticleCategoryRelationEntity::getArticleId, id)
                        .list();
                TbArticleCategoryRelationEntity tbArticleCategoryRelationEntity = list.get(0);
                if (tbArticleCategoryRelationEntity == null) {
                    log.debug("更新文章失败，失败原因，需要删除的分类不存在，deletedCategoryId = {}", deletedCategoryId);
                    throw new BusinessException(ErrorMessageEnum.ARTICLE_CATEGORY_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_CATEGORY_NOT_EXISTS.getIndex());
                }
                tbArticleCategoryRelationEntityService.removeById(tbArticleCategoryRelationEntity);
            }
        }

        // 检查categoryIds， 筛选出目前数据库没有categoryIds
        HashSet<Integer> notSaveCategoryIds = new HashSet<>();
        for (Integer categoryId : categoryIds) {
            // 检查分类是否存在
            iCategoryService.lockById(categoryId).orElseThrow(() -> {
                log.debug("更新文章失败，失败原因，分类不存在，categoryId = {}", categoryId);
                return new BusinessException(ErrorMessageEnum.CATEGORY_NOT_EXISTS.getMessage(), ErrorMessageEnum.CATEGORY_NOT_EXISTS.getIndex());
            });
            List<TbArticleCategoryRelationEntity> categoryEntities = tbArticleCategoryRelationEntityService.lambdaQuery().eq(TbArticleCategoryRelationEntity::getCategoryId, categoryId).eq(TbArticleCategoryRelationEntity::getArticleId, id).list();
            if (categoryEntities.isEmpty()) {
                notSaveCategoryIds.add(categoryId);
            }
        }

        // 检查tags， 筛选出目前数据库没有tag
        HashSet<String> notSaveTags = new HashSet<>();
        for (String tag : tags) {
            List<TbArticleTagEntity> tbArticleTagEntityList = tbArticleTagEntityService.lambdaQuery().eq(TbArticleTagEntity::getTagName, tag).eq(TbArticleTagEntity::getArticleId, id).list();
            if (tbArticleTagEntityList.isEmpty()) {
                notSaveTags.add(tag);
            }
        }

        // 检查deleteTags
        for (Integer deletedTagId : deletedTagIds) {
            TbArticleTagEntity tbArticleTagEntity = iArticleTagService.lockById(deletedTagId).orElseThrow(() -> {
                log.debug("更新文章失败，失败原因，需要删除的Tag id不存在，deletedTagId = {}", deletedTagId);
                return new BusinessException(ErrorMessageEnum.ARTICLE_TAG_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_TAG_NOT_EXISTS.getIndex());
            });
            tbArticleTagEntity.setIsDelete(RecordRemoveStatusEnum._LOGICALLY_DELETED.getIndex());
            tbArticleTagEntityService.removeById(tbArticleTagEntity);
        }
        tbArticleEntity.setArticleType(articleType);
        tbArticleEntity.setStatus(status);
        boolean isOk = tbArticleEntityService.updateById(tbArticleEntity);

        if (isOk) {

            if (!notSaveFileIds.isEmpty()) {
                for (Integer fileId : notSaveFileIds) {
                    iArticleFileService.create(tbArticleEntity.getId(), fileId);
                }
            }

            if (!notSaveCategoryIds.isEmpty()) {
                for (Integer notSaveCategoryId : notSaveCategoryIds) {
                    iArticleCategoryService.create(tbArticleEntity.getId(), notSaveCategoryId);
                }
            }

            if (!notSaveTags.isEmpty()) {
                for (String tag : notSaveTags) {
                    iArticleTagService.create(tbArticleEntity.getId(), tag);
                }
            }

            List<TbArticleSeoEntity> list = tbArticleSeoEntityService.lambdaQuery().eq(TbArticleSeoEntity::getArticleId, id).eq(TbArticleSeoEntity::getLanguageId, languageId).list();
            if (!list.isEmpty()) {
                TbArticleSeoEntity tbArticleSeoEntity = list.get(0);
                iArticleSeoService.update(tbArticleSeoEntity.getId(), languageId, metaTitle, metaDescription);
            }

            List<TbArticleDetailEntity> listDetail = tbArticleDetailEntityService.lambdaQuery().eq(TbArticleDetailEntity::getArticleId, id).eq(TbArticleDetailEntity::getLanguageId, languageId).list();
            if (!listDetail.isEmpty()) {
                TbArticleDetailEntity tbArticleDetailEntity = listDetail.get(0);
                iArticleDetailService.update(tbArticleDetailEntity.getId(), languageId, articleName, articleDescription, customs);
            }

        }

        return tbArticleEntity;
    }

    /**
     * 锁表查询单条记录
     *
     * @param id
     * @return
     */
    @Override
//    @Cacheable(value = "article", key = "#id")
    public Optional<TbArticleEntity> lockById(Serializable id) {
        return Optional.ofNullable(iArticleDao.lockById(id));
    }


    public List<TbArticleEntity> lockByIds(List<Integer> ids) {
        return iArticleDao.lockByIds(ids);
    }


    @Override
//    @Cacheable(value = "article", key = "#id")
    public Optional<ArticleListResultDto> getById(Integer id) throws BusinessException {
        this.lockById(id).orElseThrow(() -> {
            log.warn("查看分类失败，分类不存在，id = {}", id);
            return new BusinessException(ErrorMessageEnum.ARTICLE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_NOT_EXISTS.getIndex());
        });
        ArticleQueryConditionDto articleQueryConditionDto = new ArticleQueryConditionDto();
        articleQueryConditionDto.setArticleId(id);
        List<ArticleListResultDto> list = this.list(articleQueryConditionDto);
        return Optional.ofNullable(!list.isEmpty() ? list.get(0) : null);
    }

    /**
     * 公共列表 - 不带登录态
     *
     * @param articleQueryConditionDto
     * @return
     */
    @Override
    public List<ArticleListResultDto> list(ArticleQueryConditionDto articleQueryConditionDto) {
        ArticleListConditionPo articleListConditionPo = MiscUtils.copyProperties(articleQueryConditionDto, ArticleListConditionPo.class);
        List<ArticleListResultDo> list = iArticleDao.list(articleListConditionPo);
        return getArticleListResultDtos(list);
    }

    private List<ArticleListResultDto> getArticleListResultDtos(List<ArticleListResultDo> list) {
        for (ArticleListResultDo articleListResultDo : list) {

            // 查找文件
            List<TbArticleFileRelationEntity> tbArticleFileEntityList = tbArticleFileRelationEntityService.lambdaQuery().eq(TbArticleFileRelationEntity::getArticleId, articleListResultDo.getId()).list();
            ArrayList<TbFileEntity> tbFileEntities = new ArrayList<>();
            if (!tbArticleFileEntityList.isEmpty()) {
                for (TbArticleFileRelationEntity tbArticleFileEntity : tbArticleFileEntityList) {
                    TbFileEntity tbFileEntityServiceById = tbFileEntityService.getById(tbArticleFileEntity.getFileId());
                    tbFileEntities.add(tbFileEntityServiceById);
                }
                articleListResultDo.setFileList(tbFileEntities);
            }

            // Tags

            List<TbArticleTagEntity> tbArticleTagEntityList = tbArticleTagEntityService.lambdaQuery().eq(TbArticleTagEntity::getArticleId, articleListResultDo.getId()).list();
            articleListResultDo.setTags(tbArticleTagEntityList);

            // 分类
            List<TbArticleCategoryRelationEntity> tbArticleCategoryEntities = tbArticleCategoryRelationEntityService.lambdaQuery().eq(TbArticleCategoryRelationEntity::getArticleId, articleListResultDo.getId()).list();
            List<List<Integer>> categoryIds = new ArrayList<>();
            if (tbArticleCategoryEntities.size() > 0) {
                for (TbArticleCategoryRelationEntity tbArticleCategoryEntity : tbArticleCategoryEntities) {
                    List<Integer> allParentCategoryIds = findAllParentCategoryIds(tbArticleCategoryEntity.getCategoryId());
                    categoryIds.add(allParentCategoryIds);
                }
            }
            articleListResultDo.setCategoryIds(categoryIds);
        }

        return MiscUtils.copyList(list, ArticleListResultDto.class);
    }

    public List<Integer> findAllParentCategoryIds(int categoryId) {
        List<Integer> parentIds = new ArrayList<>();
        // Find the parent ID of the current category
        List<TbCategoryEntity> list = tbCategoryEntityService.lambdaQuery()
                .eq(TbCategoryEntity::getId, categoryId)
                .list();
        if (!list.isEmpty()) {
            TbCategoryEntity tbCategoryEntity = list.get(0);
            Integer parentId = tbCategoryEntity.getParentId();
            // Recursively find parent category IDs
            if (parentId != 0) {
                parentIds.addAll(findAllParentCategoryIds(parentId));
            }
            // Add the current category ID to the result list (at the end)
            parentIds.add(categoryId);
        }
        return parentIds;
    }

    /**
     * 公共列表-带分页 - 不带登录态
     *
     * @param articleQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public IPage<ArticleListResultDto> page(ArticleQueryConditionDto articleQueryConditionDto, int pageNumber, int pageSize) {
        Page<ArticleQueryConditionDto> page = PageHelper.startPage(pageNumber, pageSize);
//        PageHelper.orderBy(String.format("%s desc", TbArticleEntity.ID));
        List<ArticleListResultDto> articleListResultDtoList = list(articleQueryConditionDto);
        IPage<ArticleListResultDto> linkListResultDtoPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        linkListResultDtoPage.setTotal(page.getTotal());
        linkListResultDtoPage.setRecords(articleListResultDtoList);
        return linkListResultDtoPage;
    }

}
