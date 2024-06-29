/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.entity.TbArticleCategoryRelationEntity;
import com.fulinx.spring.service.article.dto.ArticleCategoryListResultDto;
import com.fulinx.spring.service.article.dto.ArticleCategoryQueryConditionDto;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IArticleCategoryService {

    /**
     * @return
     * @throws BusinessException
     */
    Optional<TbArticleCategoryRelationEntity> create(Integer articleId, Integer categoryId) throws BusinessException;

    Boolean remove(List<Integer> ids) throws BusinessException;

    Boolean update(Integer articleId, Integer categoryId) throws BusinessException;

    /**
     * 锁表查询单条记录
     *
     * @param id
     * @return
     */
    Optional<TbArticleCategoryRelationEntity> lockById(Serializable id);

    /**
     * 获取单条记录
     *
     * @param id
     * @return
     */
    Optional<ArticleCategoryListResultDto> getById(Integer id) throws BusinessException;

    /**
     * 列表查询
     *
     * @param articleCategoryQueryConditionDto
     * @return
     */
    List<ArticleCategoryListResultDto> list(ArticleCategoryQueryConditionDto articleCategoryQueryConditionDto);

    /**
     * 列表带分页查询
     *
     * @param articleCategoryQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    IPage<ArticleCategoryListResultDto> page(ArticleCategoryQueryConditionDto articleCategoryQueryConditionDto, int pageNumber, int pageSize);

}
