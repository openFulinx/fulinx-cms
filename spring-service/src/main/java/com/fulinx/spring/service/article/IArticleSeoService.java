/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.article;

import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.entity.TbArticleSeoEntity;

import java.util.Optional;

public interface IArticleSeoService {

    /**
     * Create ArticleSeo Seo
     *
     * @param articleId
     * @param metaTitle
     * @param metaDescription
     * @return
     * @throws BusinessException
     */
    Optional<TbArticleSeoEntity> create(Integer articleId, Integer languageId, String metaTitle, String metaDescription) throws BusinessException;

    /**
     * Remove ArticleSeo Seo
     *
     * @param articleSeoId
     * @return
     * @throws BusinessException
     */
    Boolean remove(Integer articleSeoId) throws BusinessException;

    /**
     * Update ArticleSeo Seo
     *
     * @param articleId
     * @param metaTitle
     * @param metaDescription
     * @return
     * @throws BusinessException
     */
    boolean update(Integer articleId, Integer languageId, String metaTitle, String metaDescription) throws BusinessException;


}
