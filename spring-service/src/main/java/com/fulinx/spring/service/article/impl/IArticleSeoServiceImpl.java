/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.article.impl;

import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.dao.mapper.IArticleSeoDao;
import com.fulinx.spring.data.mysql.entity.TbArticleSeoEntity;
import com.fulinx.spring.data.mysql.service.TbArticleSeoEntityService;
import com.fulinx.spring.service.article.IArticleSeoService;
import com.fulinx.spring.service.article.IArticleService;
import com.fulinx.spring.service.enums.ErrorMessageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

@Slf4j
@Service
public class IArticleSeoServiceImpl implements IArticleSeoService {

    private final IArticleService iArticleService;

    private final TbArticleSeoEntityService tbArticleSeoEntityService;

    private final IArticleSeoDao iArticleSeoDao;

    @Lazy
    @Autowired
    public IArticleSeoServiceImpl(IArticleService iArticleService, TbArticleSeoEntityService tbArticleSeoEntityService, IArticleSeoDao iArticleSeoDao) {
        this.iArticleService = iArticleService;
        this.tbArticleSeoEntityService = tbArticleSeoEntityService;
        this.iArticleSeoDao = iArticleSeoDao;
    }


    /**
     * Create ArticleSeo Seo
     *
     * @param articleId
     * @param metaTitle
     * @param metaDescription
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Optional<TbArticleSeoEntity> create(Integer articleId, String metaTitle,  String metaDescription) throws BusinessException {
        // 检查文章是否存在
        iArticleService.lockById(articleId).orElseThrow(() -> {
            log.debug("新增文章SEO失败，失败原因，文章不存在，articleId = {}", articleId);
            return new BusinessException(ErrorMessageEnum.ARTICLE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_NOT_EXISTS.getIndex());
        });

        TbArticleSeoEntity tbArticleSeoEntity = new TbArticleSeoEntity();
        tbArticleSeoEntity.setArticleId(articleId);
        tbArticleSeoEntity.setMetaTitle(metaTitle);
        tbArticleSeoEntity.setMetaDescription(metaDescription);
        boolean isOk = tbArticleSeoEntityService.save(tbArticleSeoEntity);
        return Optional.ofNullable(isOk ? tbArticleSeoEntity : null);
    }

    /**
     * Remove ArticleSeo Seo
     *
     * @param articleSeoId
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean remove(Integer articleSeoId) throws BusinessException {
        return tbArticleSeoEntityService.removeById(articleSeoId);
    }

    public Optional<TbArticleSeoEntity> lockById(Serializable id) {
        return Optional.ofNullable(iArticleSeoDao.lockById(id));
    }

    /**
     * Update ArticleSeo Seo
     *
     * @param articleSeoId
     * @param metaTitle
     * @param metaDescription
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(Integer articleSeoId, String metaTitle,  String metaDescription) throws BusinessException {
        // 检查文章是否存在
        TbArticleSeoEntity tbArticleSeoEntity = this.lockById(articleSeoId).orElseThrow(() -> {
            log.debug("新增文章SEO失败，失败原因，文章SEO不存在，articleSeoId = {}", articleSeoId);
            return new BusinessException(ErrorMessageEnum.ARTICLE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_NOT_EXISTS.getIndex());
        });

        tbArticleSeoEntity.setMetaTitle(metaTitle);
        tbArticleSeoEntity.setMetaDescription(metaDescription);
        return tbArticleSeoEntityService.updateById(tbArticleSeoEntity);
    }
}
