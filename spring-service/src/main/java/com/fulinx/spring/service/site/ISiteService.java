/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.site;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.entity.TbSiteEntity;
import com.fulinx.spring.service.site.dto.SiteListResultDto;
import com.fulinx.spring.service.site.dto.SiteQueryConditionDto;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface ISiteService {

    Optional<TbSiteEntity> create(String domain, Integer languageId, String siteName, String metaTitle, String metaDescription, Integer logoFileId, Integer faviconFileId, Boolean status) throws BusinessException;

    /**
     * 删除文件
     *
     * @param siteIds
     * @return
     * @throws BusinessException
     */
    boolean remove(List<Integer> siteIds) throws BusinessException;


    boolean update(Integer id,String domain, Integer languageId, String siteName, String metaTitle, String metaDescription, Integer logoFileId, Integer faviconFileId, Boolean status) throws BusinessException;

    /**
     * 锁表查
     *
     * @param id
     * @return
     */
    Optional<TbSiteEntity> lockById(Serializable id);

    /**
     * 查询单个
     *
     * @param id
     * @return
     */
    Optional<SiteListResultDto> getById(Integer id) throws BusinessException;

    /**
     * 查询列表
     *
     * @param siteQueryConditionDto
     * @return
     */
    List<SiteListResultDto> list(SiteQueryConditionDto siteQueryConditionDto);

    /**
     * 列表-分页
     *
     * @param siteQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    IPage<SiteListResultDto> page(SiteQueryConditionDto siteQueryConditionDto, int pageNumber, int pageSize);

}
