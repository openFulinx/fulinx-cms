/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.category;

import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.entity.TbCategoryDetailEntity;

import java.io.Serializable;
import java.util.Optional;

public interface ICategoryDescriptionService {

    /**
     * Create Category Description
     * @return
     * @throws BusinessException
     */
    Optional<TbCategoryDetailEntity> create(Integer categoryId, String categoryName, String categoryDescription) throws BusinessException;

    /**
     * Remove Category Description
     * @param categoryId
     * @return
     * @throws BusinessException
     */
    Boolean remove(Integer categoryId) throws BusinessException;

    /**
     * Update Category Description
     * @param categoryId
     * @param categoryName
     * @param categoryDescription
     * @return
     * @throws BusinessException
     */
    boolean update(Integer categoryId, String categoryName, String categoryDescription) throws BusinessException;

    /**
     * 锁表查询单条记录
     *
     * @param categoryId
     * @return
     */
    Optional<TbCategoryDetailEntity> lockByCategoryId(Serializable categoryId);

}
