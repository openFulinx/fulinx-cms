/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.category.impl;

import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.dao.mapper.ICategoryDescriptionDao;
import com.fulinx.spring.data.mysql.entity.TbCategoryDetailEntity;
import com.fulinx.spring.data.mysql.service.TbCategoryDetailEntityService;
import com.fulinx.spring.service.category.ICategoryDescriptionService;
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
public class ICategoryDescriptionServiceImpl implements ICategoryDescriptionService {

    private final TbCategoryDetailEntityService tbCategoryDetailEntityService;

    private final ICategoryDescriptionDao iCategoryDescriptionDao;

    @Lazy
    @Autowired
    public ICategoryDescriptionServiceImpl(TbCategoryDetailEntityService tbCategoryDetailEntityService, ICategoryDescriptionDao iCategoryDescriptionDao) {
        this.tbCategoryDetailEntityService = tbCategoryDetailEntityService;
        this.iCategoryDescriptionDao = iCategoryDescriptionDao;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Optional<TbCategoryDetailEntity> create(Integer categoryId, String categoryName, String categoryDescription) throws BusinessException {
        TbCategoryDetailEntity tbCategoryDetailEntity = new TbCategoryDetailEntity();
        tbCategoryDetailEntity.setCategoryId(categoryId);
        tbCategoryDetailEntity.setCategoryName(categoryName);
        tbCategoryDetailEntity.setCategoryDescription(categoryDescription);
        Boolean isOk = tbCategoryDetailEntityService.save(tbCategoryDetailEntity);
        return Optional.ofNullable(isOk ? tbCategoryDetailEntity : null);
    }

    /**
     * Remove Category Description
     *
     * @param categoryId
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean remove(Integer categoryId) throws BusinessException {
        TbCategoryDetailEntity tbCategoryDetailEntity = this.lockByCategoryId(categoryId).orElseThrow(() -> {
            log.warn("删除分类描述失败，分类描述不存在，id = {}", categoryId);
            return new BusinessException(ErrorMessageEnum.CATEGORY_NOT_EXISTS.getMessage(), ErrorMessageEnum.CATEGORY_NOT_EXISTS.getIndex());
        });
        tbCategoryDetailEntityService.removeById(tbCategoryDetailEntity);

        return true;
    }

    /**
     * Update Category Description
     *
     * @param categoryId
     * @param categoryName
     * @param categoryDescription
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(Integer categoryId, String categoryName, String categoryDescription) throws BusinessException {
        TbCategoryDetailEntity tbCategoryDetailEntity = this.lockByCategoryId(categoryId).orElseThrow(() -> {
            log.warn("更新分类描述失败，分类描述不存在，id = {}", categoryId);
            return new BusinessException(ErrorMessageEnum.CATEGORY_NOT_EXISTS.getMessage(), ErrorMessageEnum.CATEGORY_NOT_EXISTS.getIndex());
        });
        tbCategoryDetailEntity.setCategoryName(categoryName);
        tbCategoryDetailEntity.setCategoryDescription(categoryDescription);
        return tbCategoryDetailEntityService.updateById(tbCategoryDetailEntity);
    }

    /**
     * 锁表查询单条记录
     *
     * @param categoryId
     * @return
     */
    @Override
    public Optional<TbCategoryDetailEntity> lockByCategoryId(Serializable categoryId) {
        return Optional.ofNullable(iCategoryDescriptionDao.lockByCategoryId(categoryId));
    }


}
