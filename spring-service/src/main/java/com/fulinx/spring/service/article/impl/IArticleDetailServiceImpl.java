/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.article.impl;

import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.dao.mapper.IArticleDetailDao;
import com.fulinx.spring.data.mysql.entity.TbArticleDetailEntity;
import com.fulinx.spring.data.mysql.service.TbArticleDetailEntityService;
import com.fulinx.spring.service.article.IArticleDetailService;
import com.fulinx.spring.service.article.IArticleDetailService;
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
public class IArticleDetailServiceImpl implements IArticleDetailService {

    private final IArticleService iArticleService;

    private final TbArticleDetailEntityService tbArticleDetailEntityService;

    private final IArticleDetailDao iArticleDetailDao;

    @Lazy
    @Autowired
    public IArticleDetailServiceImpl(IArticleService iArticleService, TbArticleDetailEntityService tbArticleDetailEntityService, IArticleDetailDao iArticleDetailDao) {
        this.iArticleService = iArticleService;
        this.tbArticleDetailEntityService = tbArticleDetailEntityService;
        this.iArticleDetailDao = iArticleDetailDao;
    }


    /**
     * Create ArticleDetail Seo
     *
     * @param articleId
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Optional<TbArticleDetailEntity> create(Integer articleId, Integer languageId, String articleName, String description, String customs) throws BusinessException {
        // 检查文章是否存在
        iArticleService.lockById(articleId).orElseThrow(() -> {
            log.debug("新增文章SEO失败，失败原因，文章不存在，articleId = {}", articleId);
            return new BusinessException(ErrorMessageEnum.ARTICLE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_NOT_EXISTS.getIndex());
        });

        TbArticleDetailEntity tbArticleDetailEntity = new TbArticleDetailEntity();
        tbArticleDetailEntity.setLanguageId(languageId);
        tbArticleDetailEntity.setArticleId(articleId);
        tbArticleDetailEntity.setArticleName(articleName);
        tbArticleDetailEntity.setArticleDescription(description);
        tbArticleDetailEntity.setCustoms(customs);
        boolean isOk = tbArticleDetailEntityService.save(tbArticleDetailEntity);
        return Optional.ofNullable(isOk ? tbArticleDetailEntity : null);
    }

    /**
     * Remove ArticleDetail Seo
     *
     * @param articleDetailId
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean remove(Integer articleDetailId) throws BusinessException {
        return tbArticleDetailEntityService.removeById(articleDetailId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean removeByArticleId(Integer articleId) throws BusinessException {
        iArticleService.lockById(articleId).orElseThrow(() -> {
            log.debug("删除文章SEO失败，失败原因，文章不存在，articleId = {}", articleId);
            return new BusinessException(ErrorMessageEnum.ARTICLE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_NOT_EXISTS.getIndex());
        });
        tbArticleDetailEntityService.lambdaQuery().eq(TbArticleDetailEntity::getArticleId, articleId).list().forEach(tbArticleDetailEntity -> {
            tbArticleDetailEntityService.removeById(tbArticleDetailEntity.getId());
        });
        return true;
    }

    public Optional<TbArticleDetailEntity> lockById(Serializable id) {
        return Optional.ofNullable(iArticleDetailDao.lockById(id));
    }

    /**
     * Update ArticleDetail Seo
     *
     * @param articleDetailId
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(Integer articleDetailId, Integer languageId, String articleName, String description, String customs) throws BusinessException {
        // 检查文章是否存在
        TbArticleDetailEntity tbArticleDetailEntity = this.lockById(articleDetailId).orElseThrow(() -> {
            log.debug("新增文章SEO失败，失败原因，文章SEO不存在，articleDetailId = {}", articleDetailId);
            return new BusinessException(ErrorMessageEnum.ARTICLE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_NOT_EXISTS.getIndex());
        });
        tbArticleDetailEntity.setLanguageId(languageId);
        tbArticleDetailEntity.setArticleName(articleName);
        tbArticleDetailEntity.setArticleDescription(description);
        tbArticleDetailEntity.setCustoms(customs);
        return tbArticleDetailEntityService.updateById(tbArticleDetailEntity);
    }
}
