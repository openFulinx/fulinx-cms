/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.site.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.data.enums.RecordRemoveStatusEnum;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.dao.mapper.ISiteDao;
import com.fulinx.spring.data.mysql.dao.podo.site.SiteListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.site.SiteListResultDo;
import com.fulinx.spring.data.mysql.entity.TbSiteEntity;
import com.fulinx.spring.data.mysql.service.TbFileEntityService;
import com.fulinx.spring.data.mysql.service.TbSiteEntityService;
import com.fulinx.spring.service.enums.ErrorMessageEnum;
import com.fulinx.spring.service.site.ISiteService;
import com.fulinx.spring.service.site.dto.SiteListResultDto;
import com.fulinx.spring.service.site.dto.SiteQueryConditionDto;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ISiteServiceImpl implements ISiteService {
    private final TbSiteEntityService tbSiteEntityService;

    private final TbFileEntityService tbFileEntityService;

    private final ISiteDao iSiteDao;

    @Lazy
    @Autowired
    public ISiteServiceImpl(TbSiteEntityService tbSiteEntityService, TbFileEntityService tbFileEntityService, ISiteDao iSiteDao) {
        this.tbSiteEntityService = tbSiteEntityService;
        this.tbFileEntityService = tbFileEntityService;
        this.iSiteDao = iSiteDao;
    }


    
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Optional<TbSiteEntity> create(String domain, Integer languageId, String siteName, String metaTitle, String metaDescription, Integer logoFileId, Integer faviconFileId, Boolean status) throws BusinessException {
        TbSiteEntity tbSiteEntity = new TbSiteEntity();
        tbSiteEntity.setDomain(domain);
        tbSiteEntity.setLanguageId(languageId);
        tbSiteEntity.setSiteName(siteName);
        tbSiteEntity.setMetaTitle(metaTitle);
        tbSiteEntity.setMetaDescription(metaDescription);
        tbSiteEntity.setLogoFileId(logoFileId);
        tbSiteEntity.setFaviconFileId(faviconFileId);
        tbSiteEntity.setStatus(status);
        boolean isOk = tbSiteEntityService.save(tbSiteEntity);
        return Optional.ofNullable(isOk ? tbSiteEntity : null);
    }


    /**
     * 删除网站
     *
     * @param ids
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean remove(List<Integer> ids) throws BusinessException {
        for (Integer id : ids) {
            TbSiteEntity tbSiteEntity = this.lockById(id).orElseThrow(() -> {
                log.warn("删除网站失败，网站不存在，id = {}", id);
                return new BusinessException(ErrorMessageEnum.SITE_NOT_EXISTS.getMessage(), ErrorMessageEnum.SITE_NOT_EXISTS.getIndex());
            });
            tbSiteEntity.setIsDelete(RecordRemoveStatusEnum._LOGICALLY_DELETED.getIndex());
            tbSiteEntityService.removeById(id);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(Integer id, String domain, Integer languageId, String siteName, String metaTitle, String metaDescription, Integer logoFileId, Integer faviconFileId, Boolean status) throws BusinessException {
        TbSiteEntity tbSiteEntity = this.lockById(id).orElseThrow(() -> {
            log.warn("删除网站失败，网站不存在，id = {}", id);
            return new BusinessException(ErrorMessageEnum.SITE_NOT_EXISTS.getMessage(), ErrorMessageEnum.SITE_NOT_EXISTS.getIndex());
        });
        tbSiteEntity.setDomain(domain);
        tbSiteEntity.setLanguageId(languageId);
        tbSiteEntity.setSiteName(siteName);
        tbSiteEntity.setMetaTitle(metaTitle);
        tbSiteEntity.setMetaDescription(metaDescription);
        tbSiteEntity.setLogoFileId(logoFileId);
        tbSiteEntity.setFaviconFileId(faviconFileId);
        tbSiteEntity.setStatus(status);
        return tbSiteEntityService.updateById(tbSiteEntity);
    }


    /**
     * 锁表查
     *
     * @param id
     * @return
     */
    @Override
    public Optional<TbSiteEntity> lockById(Serializable id) {
        return Optional.ofNullable(iSiteDao.lockById(id));
    }

    /**
     * 查询单个
     *
     * @param id
     * @return
     */
    @Override
    public Optional<SiteListResultDto> getById(Integer id) throws BusinessException {
        this.lockById(id).orElseThrow(() -> {
            log.warn("查看网站失败，网站不存在，id = {}", id);
            return new BusinessException(ErrorMessageEnum.SITE_NOT_EXISTS.getMessage(), ErrorMessageEnum.SITE_NOT_EXISTS.getIndex());
        });
        SiteQueryConditionDto siteQueryConditionDto = new SiteQueryConditionDto();
        siteQueryConditionDto.setId(id);
        IPage<SiteListResultDto> siteQueryResultDtoIPage = this.page(siteQueryConditionDto, 1, 1);
        return Optional.ofNullable(siteQueryResultDtoIPage.getTotal() == 0 ? null : siteQueryResultDtoIPage.getRecords().get(0));
    }

    /**
     * 查询列表
     *
     * @param siteQueryConditionDto
     * @return
     */
    @Override
    public List<SiteListResultDto> list(SiteQueryConditionDto siteQueryConditionDto) {
        SiteListConditionPo siteListConditionPo = MiscUtils.copyProperties(siteQueryConditionDto, SiteListConditionPo.class);
        List<SiteListResultDo> siteListResultDoList = iSiteDao.list(siteListConditionPo);
        siteListResultDoList.forEach(siteListResultDo -> {
            siteListResultDo.setLogoFileVo(tbFileEntityService.getById(siteListResultDo.getLogoFileId()));
            siteListResultDo.setFaviconFileVo(tbFileEntityService.getById(siteListResultDo.getFaviconFileId()));
        });
        return MiscUtils.copyList(siteListResultDoList, SiteListResultDto.class);
    }

    /**
     * 列表-分页
     *
     * @param siteQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public IPage<SiteListResultDto> page(SiteQueryConditionDto siteQueryConditionDto, int pageNumber, int pageSize) {
        Page<SiteListResultDo> page = PageHelper.startPage(pageNumber, pageSize);
        PageHelper.orderBy(String.format("%s desc", TbSiteEntity.ID));
        List<SiteListResultDto> siteQueryResultDtoList = list(siteQueryConditionDto);
        IPage<SiteListResultDto> siteQueryResultDtoIPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        siteQueryResultDtoIPage.setTotal(page.getTotal());
        siteQueryResultDtoIPage.setRecords(siteQueryResultDtoList);
        return siteQueryResultDtoIPage;
    }
}
