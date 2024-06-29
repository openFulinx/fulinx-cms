/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.category;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.entity.TbCategoryEntity;
import com.fulinx.spring.service.category.dto.CategoryArticleListResultDto;
import com.fulinx.spring.service.category.dto.CategoryListResultDto;
import com.fulinx.spring.service.category.dto.CategoryQueryConditionDto;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    /**
     * @return
     * @throws BusinessException
     */
    Optional<CategoryListResultDto> create(Integer parentId,  String categoryName, String categoryDescription, String metaTitle,  String metaDescription, Integer fileId, Boolean status) throws BusinessException;

    Boolean remove(List<Integer> ids) throws BusinessException;

    Optional<CategoryListResultDto> update(Integer id, Integer parentId,  String categoryName, String categoryDescription, String metaTitle, String metaDescription, Integer fileId, Boolean status) throws BusinessException;


    /**
     * 锁表查询单条记录
     *
     * @param id
     * @return
     */
    Optional<TbCategoryEntity> lockById(Serializable id);

    /**
     * 获取单条记录
     *
     * @param id
     * @return
     */
    Optional<CategoryListResultDto> getById(Integer id) throws BusinessException;

    /**
     * 列表查询
     *
     * @param categoryQueryConditionDto
     * @return
     */
    List<CategoryListResultDto> list(CategoryQueryConditionDto categoryQueryConditionDto);

    List<CategoryArticleListResultDto> listCategoryArticle();

    /**
     * 列表带分页查询
     *
     * @param categoryQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    IPage<CategoryListResultDto> page(CategoryQueryConditionDto categoryQueryConditionDto, int pageNumber, int pageSize);

}
