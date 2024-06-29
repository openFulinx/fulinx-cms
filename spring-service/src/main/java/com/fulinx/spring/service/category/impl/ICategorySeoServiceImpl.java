/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.category.impl;

import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.dao.mapper.ICategorySeoDao;
import com.fulinx.spring.data.mysql.entity.TbCategorySeoEntity;
import com.fulinx.spring.data.mysql.service.TbCategorySeoEntityService;
import com.fulinx.spring.service.category.ICategorySeoService;
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
public class ICategorySeoServiceImpl implements ICategorySeoService {
    private final TbCategorySeoEntityService tbCategorySeoEntityService;

    private final ICategorySeoDao iCategorySeoDao;

    @Lazy
    @Autowired
    public ICategorySeoServiceImpl(TbCategorySeoEntityService tbCategorySeoEntityService, ICategorySeoDao iCategorySeoDao) {
        this.tbCategorySeoEntityService = tbCategorySeoEntityService;
        this.iCategorySeoDao = iCategorySeoDao;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Optional<TbCategorySeoEntity> create(Integer categoryId, String metaTitle, String metaDescription) throws BusinessException {
        TbCategorySeoEntity tbCategorySeoEntity = new TbCategorySeoEntity();
        tbCategorySeoEntity.setCategoryId(categoryId);
        tbCategorySeoEntity.setMetaTitle(metaTitle);
        tbCategorySeoEntity.setMetaDescription(metaDescription);
        Boolean isOk = tbCategorySeoEntityService.save(tbCategorySeoEntity);
        return Optional.ofNullable(isOk ? tbCategorySeoEntity : null);
    }

    /**
     * Remove Category Seo
     *
     * @param categoryId
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean remove(Integer categoryId) throws BusinessException {
        TbCategorySeoEntity tbCategorySeoEntity = this.lockByCategoryId(categoryId).orElseThrow(() -> {
            log.warn("删除分类Seo失败，分类Seo不存在，id = {}", categoryId);
            return new BusinessException(ErrorMessageEnum.CATEGORY_SEO_NOT_EXISTS.getMessage(), ErrorMessageEnum.CATEGORY_SEO_NOT_EXISTS.getIndex());
        });
        tbCategorySeoEntityService.removeById(tbCategorySeoEntity);
        return true;
    }

    /**
     * Update Category Seo
     *
     * @param categoryId
     * @param metaTitle
     * @param metaDescription
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(Integer categoryId, String metaTitle, String metaDescription) throws BusinessException {
        TbCategorySeoEntity tbCategorySeoEntity = this.lockByCategoryId(categoryId).orElseThrow(() -> {
            log.warn("更新分类描述失败，分类描述不存在，id = {}", categoryId);
            return new BusinessException(ErrorMessageEnum.CATEGORY_SEO_NOT_EXISTS.getMessage(), ErrorMessageEnum.CATEGORY_SEO_NOT_EXISTS.getIndex());
        });
        tbCategorySeoEntity.setMetaTitle(metaTitle);
        tbCategorySeoEntity.setMetaDescription(metaDescription);
        return tbCategorySeoEntityService.updateById(tbCategorySeoEntity);
    }


    /**
     * 锁表查询单条记录
     *
     * @param categoryId
     * @return
     */
    @Override
    public Optional<TbCategorySeoEntity> lockByCategoryId(Serializable categoryId) {
        return Optional.ofNullable(iCategorySeoDao.lockByCategoryId(categoryId));
    }
}
