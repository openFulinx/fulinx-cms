/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.article;

import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.entity.TbArticleDetailEntity;

import java.util.Optional;

public interface IArticleDetailService {

    /**
     * Create ArticleSeo Seo
     *
     * @param articleId
     * @return
     * @throws BusinessException
     */
    Optional<TbArticleDetailEntity> create(Integer articleId, Integer languageId, String articleName, String description, String customs) throws BusinessException;

    /**
     * Remove ArticleSeo Seo
     *
     * @param articleDetailId
     * @return
     * @throws BusinessException
     */
    Boolean remove(Integer articleDetailId) throws BusinessException;

    Boolean removeByArticleId(Integer articleId) throws BusinessException;

    /**
     * Update ArticleSeo Seo
     *
     * @param articleDetailId
     * @return
     * @throws BusinessException
     */
    boolean update(Integer articleDetailId, Integer languageId, String articleName, String description, String customs) throws BusinessException;


}
