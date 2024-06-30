/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.category;

import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.entity.TbCategorySeoEntity;

import java.io.Serializable;
import java.util.Optional;

public interface ICategorySeoService {

    /**
     * Create Category Seo
     *
     * @param categoryId
     * @param metaTitle
     * @param metaDescription
     * @return
     * @throws BusinessException
     */
    Optional<TbCategorySeoEntity> create(Integer categoryId, Integer languageId, String metaTitle, String metaDescription) throws BusinessException;

    /**
     * Remove Category Seo
     *
     * @param categoryId
     * @return
     * @throws BusinessException
     */
    Boolean remove(Integer categoryId) throws BusinessException;

    /**
     * Update Category Seo
     *
     * @param categoryId
     * @param metaTitle
     * @param metaDescription
     * @return
     * @throws BusinessException
     */
    boolean update(Integer categoryId,Integer languageId, String metaTitle, String metaDescription) throws BusinessException;


    /**
     * Get Locked Category Seo
     *
     * @param categoryId
     * @return
     */
    Optional<TbCategorySeoEntity> lockByCategoryId(Serializable categoryId);

}
