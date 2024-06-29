/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.category.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.data.mysql.enums.SimpleStatusEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.dao.mapper.IArticleDao;
import com.fulinx.spring.data.mysql.dao.mapper.ICategoryDao;
import com.fulinx.spring.data.mysql.dao.podo.article.ArticleListResultDo;
import com.fulinx.spring.data.mysql.dao.podo.category.CategoryListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.category.CategoryListResultDo;
import com.fulinx.spring.data.mysql.entity.*;
import com.fulinx.spring.data.mysql.service.*;
import com.fulinx.spring.service.article.dto.ArticleListResultDto;
import com.fulinx.spring.service.category.ICategoryDescriptionService;
import com.fulinx.spring.service.category.ICategorySeoService;
import com.fulinx.spring.service.category.ICategoryService;
import com.fulinx.spring.service.category.dto.CategoryArticleListResultDto;
import com.fulinx.spring.service.category.dto.CategoryListResultDto;
import com.fulinx.spring.service.category.dto.CategoryQueryConditionDto;
import com.fulinx.spring.service.enums.ErrorMessageEnum;
import com.fulinx.spring.service.file.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

import static com.github.houbb.heaven.util.lang.StringUtil.trim;

@Slf4j
@Service
public class ICategoryServiceImpl implements ICategoryService {
    private final TbCategoryEntityService tbCategoryEntityService;

    private final ICategoryDao iCategoryDao;

    private final ICategoryDescriptionService iCategoryDescriptionService;

    private final ICategorySeoService iCategorySeoService;

    private final IFileService iFileService;

    private final TbFileEntityService tbFileEntityService;

    private final TbArticleCategoryRelationEntityService tbArticleCategoryRelationEntityService;

    private final TbArticleEntityService tbArticleEntityService;

    private final IArticleDao iArticleDao;

    private final TbUserProfileEntityService tbUserProfileEntityService;

    private final TbArticleFileRelationEntityService tbArticleFileRelationEntityService;

    private final TbArticleTagEntityService tbArticleTagEntityService;

    @Lazy
    @Autowired
    public ICategoryServiceImpl(TbCategoryEntityService tbCategoryEntityService, ICategoryDao iCategoryDao, ICategoryDescriptionService iCategoryDescriptionService, ICategorySeoService iCategorySeoService, IFileService iFileService, TbFileEntityService tbFileEntityService, TbArticleCategoryRelationEntityService tbArticleCategoryRelationEntityService, TbArticleEntityService tbArticleEntityService, IArticleDao iArticleDao, TbUserProfileEntityService tbUserProfileEntityService, TbArticleFileRelationEntityService tbArticleFileRelationEntityService, TbArticleTagEntityService tbArticleTagEntityService) {
        this.tbCategoryEntityService = tbCategoryEntityService;
        this.iCategoryDao = iCategoryDao;
        this.iCategoryDescriptionService = iCategoryDescriptionService;
        this.iCategorySeoService = iCategorySeoService;
        this.iFileService = iFileService;
        this.tbFileEntityService = tbFileEntityService;
        this.tbArticleCategoryRelationEntityService = tbArticleCategoryRelationEntityService;
        this.tbArticleEntityService = tbArticleEntityService;
        this.iArticleDao = iArticleDao;
        this.tbUserProfileEntityService = tbUserProfileEntityService;
        this.tbArticleFileRelationEntityService = tbArticleFileRelationEntityService;
        this.tbArticleTagEntityService = tbArticleTagEntityService;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
//    @CachePut(value = "category", key = "#id")
    public Optional<CategoryListResultDto> create(Integer parentId,  String categoryName, String categoryDescription, String metaTitle,  String metaDescription, Integer fileId, Boolean status) throws BusinessException {
        if (fileId != null) {
            // 检查FileId是否存在
            iFileService.lockById(fileId).orElseThrow(() -> {
                log.warn("新建分类失败，失败原因，文件不存在， fileId= {}", fileId);
                return new BusinessException(ErrorMessageEnum.FILE_NOT_EXISTS.getMessage(), ErrorMessageEnum.FILE_NOT_EXISTS.getIndex());
            });
        }

        TbCategoryEntity tbCategoryEntity = new TbCategoryEntity();
        tbCategoryEntity.setParentId(parentId);
        tbCategoryEntity.setFileId(fileId);
        tbCategoryEntity.setStatus(status);
        Boolean isOk = tbCategoryEntityService.save(tbCategoryEntity);
        // 新增分类描述
        iCategoryDescriptionService.create(tbCategoryEntity.getId(), trim(categoryName), categoryDescription);
        // 新增分类SEO
        iCategorySeoService.create(tbCategoryEntity.getId(), trim(metaTitle), trim(metaDescription));
        return isOk ? this.getById(tbCategoryEntity.getId()) : null;
    }

    /**
     * @param ids
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
//    @CacheEvict(value = "category", key = "#p0")
    public Boolean remove(List<Integer> ids) throws BusinessException {
        for (Integer id : ids) {
            TbCategoryEntity tbCategoryEntity = this.lockById(id).orElseThrow(() -> {
                log.warn("删除分类失败，分类不存在，id = {}", id);
                return new BusinessException(ErrorMessageEnum.CATEGORY_NOT_EXISTS.getMessage(), ErrorMessageEnum.CATEGORY_NOT_EXISTS.getIndex());
            });
            tbCategoryEntityService.removeById(tbCategoryEntity);
            // 删除分类描述
            iCategoryDescriptionService.remove(id);
            // 删除分类SEO
            iCategorySeoService.remove(id);
        }
        return null;
    }

    /**
     * @param id
     * @param parentId
     * @param categoryName
     * @param categoryDescription
     * @param metaTitle
     * @param metaDescription
     * @param fileId
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
//    @CachePut(value = "category", key = "#id")
    public Optional<CategoryListResultDto> update(Integer id, Integer parentId, String categoryName, String categoryDescription, String metaTitle,  String metaDescription, Integer fileId, Boolean status) throws BusinessException {
        TbCategoryEntity tbCategoryEntity = this.lockById(id).orElseThrow(() -> {
            log.warn("更新分类失败，分类不存在，id = {}", id);
            return new BusinessException(ErrorMessageEnum.CATEGORY_NOT_EXISTS.getMessage(), ErrorMessageEnum.CATEGORY_NOT_EXISTS.getIndex());
        });

        // 父级分类如果是自身报错
        if (parentId == id) {
            log.warn("更新分类失败，父级分类不能是自身，id = {}, parentId = {}", id, parentId);
            throw new BusinessException(ErrorMessageEnum.CATEGORY_OF_PARENT_CAN_NOT_BE_IT_SELF.getMessage(), ErrorMessageEnum.CATEGORY_OF_PARENT_CAN_NOT_BE_IT_SELF.getIndex());
        }

        if (fileId != null) {
            // 检查FileId是否存在
            iFileService.lockById(fileId).orElseThrow(() -> {
                log.warn("新建分类失败，失败原因，文件不存在， fileId= {}", fileId);
                return new BusinessException(ErrorMessageEnum.FILE_NOT_EXISTS.getMessage(), ErrorMessageEnum.FILE_NOT_EXISTS.getIndex());
            });
        }

        tbCategoryEntity.setParentId(parentId);
        tbCategoryEntity.setFileId(fileId);
        tbCategoryEntity.setStatus(status);
        // 更新分类描述
        iCategoryDescriptionService.update(id, trim(categoryName), categoryDescription);
        // 更新分类SEO
        iCategorySeoService.update(id, trim(metaTitle), trim(metaDescription));
        boolean isOk = tbCategoryEntityService.updateById(tbCategoryEntity);
        return isOk ? this.getById(tbCategoryEntity.getId()) : null;
    }


    /**
     * 锁表查询单条记录
     *
     * @param id
     * @return
     */
    @Override
//    @Cacheable(value = "category", key = "#id")
    public Optional<TbCategoryEntity> lockById(Serializable id) {
        return Optional.ofNullable(iCategoryDao.lockById(id));
    }

    /**
     * 获取单条记录
     *
     * @param id
     * @return
     */
    @Override
//    @Cacheable(value = "category", key = "#id")
    public Optional<CategoryListResultDto> getById(Integer id) throws BusinessException {
        this.lockById(id).orElseThrow(() -> {
            log.warn("查看分类失败，分类不存在，id = {}", id);
            return new BusinessException(ErrorMessageEnum.CATEGORY_NOT_EXISTS.getMessage(), ErrorMessageEnum.CATEGORY_NOT_EXISTS.getIndex());
        });
        CategoryQueryConditionDto categoryQueryConditionDto = new CategoryQueryConditionDto();
        categoryQueryConditionDto.setId(id);
        List<CategoryListResultDto> list = this.list(categoryQueryConditionDto);
        return Optional.ofNullable(list.size() > 0 ? list.get(0) : null);
    }

    /**
     * 权限列表查询
     *
     * @param categoryQueryConditionDto
     * @return
     */
    @Override
//    @Cacheable(value = "categoryList", key = "#categoryQueryConditionDto.hashCode()")
    public List<CategoryListResultDto> list(CategoryQueryConditionDto categoryQueryConditionDto) {
        CategoryListConditionPo categoryListConditionPo = MiscUtils.copyProperties(categoryQueryConditionDto, CategoryListConditionPo.class);
        List<CategoryListResultDo> list = iCategoryDao.list(categoryListConditionPo);
        List<CategoryListResultDo> categoryListResultDoList = MiscUtils.copyList(list, CategoryListResultDo.class);
        if (categoryQueryConditionDto.getId() != null) {
            return MiscUtils.copyList(buildTree(categoryListResultDoList, list.get(0).getParentId()), CategoryListResultDto.class);
        }
        return MiscUtils.copyList(buildTree(categoryListResultDoList, 0), CategoryListResultDto.class);
    }

    @Override
    public List<CategoryArticleListResultDto> listCategoryArticle() {
        CategoryListConditionPo categoryListConditionPo = new CategoryListConditionPo();
        categoryListConditionPo.setIsHomeCategory(true);
        List<CategoryListResultDo> list = iCategoryDao.list(categoryListConditionPo);
        ArrayList<CategoryArticleListResultDto> categoryArticleListResultDtoArrayList = new ArrayList<>();
        for (CategoryListResultDo categoryListResultDo : list) {
            CategoryArticleListResultDto categoryArticleListResultDto = new CategoryArticleListResultDto();
            categoryArticleListResultDto.setId(categoryListResultDo.getId());
            categoryArticleListResultDto.setCategoryName(categoryListResultDo.getCategoryName());
            TbFileEntity tbFileEntity = tbFileEntityService.getById(categoryListResultDo.getFileId());
            categoryArticleListResultDto.setCategoryFileVo(tbFileEntity);
            // 查找文章
            List<ArticleListResultDo> articleListResultDos = iArticleDao.listByCategoryId(categoryListResultDo.getId());

            for (ArticleListResultDo articleListResultDo : articleListResultDos) {
                Integer status = articleListResultDo.getStatus() == true ? 1 : 0;
                if (status.equals(SimpleStatusEnum._Enabled.getIndex())) {

                    // 查找文件
                    List<TbArticleFileRelationEntity> tbArticleFileRelationEntityList = tbArticleFileRelationEntityService.lambdaQuery().eq(TbArticleFileRelationEntity::getArticleId, articleListResultDo.getId()).list();
                    ArrayList<TbFileEntity> tbFileEntities = new ArrayList<>();
                    if (!tbArticleFileRelationEntityList.isEmpty()) {
                        for (TbArticleFileRelationEntity tbArticleFileRelationEntity : tbArticleFileRelationEntityList) {
                            TbFileEntity tbFileEntityServiceById = tbFileEntityService.getById(tbArticleFileRelationEntity.getFileId());
                            tbFileEntities.add(tbFileEntityServiceById);
                        }
                        articleListResultDo.setFileList(tbFileEntities);
                    }

                    // Tags

                    List<TbArticleTagEntity> tbArticleTagEntityList = tbArticleTagEntityService.lambdaQuery().eq(TbArticleTagEntity::getArticleId, articleListResultDo.getId()).list();
                    articleListResultDo.setTags(tbArticleTagEntityList);
                }

            }

            List<ArticleListResultDto> articleListResultDtos = MiscUtils.copyList(articleListResultDos, ArticleListResultDto.class);
            categoryArticleListResultDto.setArticles(articleListResultDtos);

            categoryArticleListResultDtoArrayList.add(categoryArticleListResultDto);
        }
        return categoryArticleListResultDtoArrayList;
    }

    /**
     * 权限列表带分页查询
     *
     * @param categoryQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
//    @Cacheable(value = "categoryPage", key = "#categoryQueryConditionDto.hashCode()")
    public IPage<CategoryListResultDto> page(CategoryQueryConditionDto categoryQueryConditionDto, int pageNumber, int pageSize) {
        Page<CategoryQueryConditionDto> page = PageHelper.startPage(pageNumber, pageSize);
        PageHelper.orderBy(String.format("%s asc", TbCategoryEntity.ID));
        List<CategoryListResultDto> categoryListResultDtoList = list(categoryQueryConditionDto);
        IPage<CategoryListResultDto> linkListResultDtoPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        linkListResultDtoPage.setTotal(page.getTotal());
        linkListResultDtoPage.setRecords(categoryListResultDtoList);
        return linkListResultDtoPage;
    }

    private List<CategoryListResultDo> buildTree(List<CategoryListResultDo> list, Integer parentId) {
        List<CategoryListResultDo> tree = new ArrayList<>();
        for (CategoryListResultDo node : list) {
            HashSet<Integer> parentIds = new HashSet<>();
            addParentIds(node.getId(), parentIds);
            node.setParentIds(new ArrayList<>(parentIds));
            TbFileEntity tbFileEntity = tbFileEntityService.getById(node.getFileId());
            if (tbFileEntity != null) {
                node.setFileVo(tbFileEntity);
            }
            if (Objects.equals(node.getParentId(), parentId)) {
                List<CategoryListResultDo> children = buildTree(list, node.getId());
                node.setChildren(children);
                tree.add(node);
            }
        }
        return tree;
    }

    private void addParentIds(Integer id, Set<Integer> parentIds) {
        TbCategoryEntity categoryEntity = tbCategoryEntityService.getById(id);
        if (categoryEntity != null && !parentIds.contains(id)) {
            parentIds.add(categoryEntity.getParentId());
            if (categoryEntity.getParentId() != 0) {
                addParentIds(categoryEntity.getParentId(), parentIds);
            }
        }
    }
}
