/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.theme.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.data.enums.RecordRemoveStatusEnum;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.dao.mapper.IThemeDao;
import com.fulinx.spring.data.mysql.dao.podo.theme.ThemeListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.theme.ThemeListResultDo;
import com.fulinx.spring.data.mysql.entity.TbThemeEntity;
import com.fulinx.spring.data.mysql.service.TbFileEntityService;
import com.fulinx.spring.data.mysql.service.TbThemeEntityService;
import com.fulinx.spring.service.enums.ErrorMessageEnum;
import com.fulinx.spring.service.theme.IThemeService;
import com.fulinx.spring.service.theme.dto.ThemeListResultDto;
import com.fulinx.spring.service.theme.dto.ThemeQueryConditionDto;
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
public class IThemeServiceImpl implements IThemeService {
    private final TbThemeEntityService tbThemeEntityService;

    private final TbFileEntityService tbFileEntityService;

    private final IThemeDao iThemeDao;

    @Lazy
    @Autowired
    public IThemeServiceImpl(TbThemeEntityService tbThemeEntityService, TbFileEntityService tbFileEntityService, IThemeDao iThemeDao) {
        this.tbThemeEntityService = tbThemeEntityService;
        this.tbFileEntityService = tbFileEntityService;
        this.iThemeDao = iThemeDao;
    }


    
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Optional<TbThemeEntity> create(String themeName, Integer themeType, String themeAuthor, String themeVersion, Integer themeThumbFileId, String themeConfig) throws BusinessException {
        TbThemeEntity tbThemeEntity = new TbThemeEntity();
        tbThemeEntity.setThemeName(themeName);
        tbThemeEntity.setThemeType(themeType);
        tbThemeEntity.setThemeAuthor(themeAuthor);
        tbThemeEntity.setThemeVersion(themeVersion);
        tbThemeEntity.setThemeThumbFileId(themeThumbFileId);
        tbThemeEntity.setThemeConfig(themeConfig);
        boolean isOk = tbThemeEntityService.save(tbThemeEntity);
        return Optional.ofNullable(isOk ? tbThemeEntity : null);
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
            TbThemeEntity tbThemeEntity = this.lockById(id).orElseThrow(() -> {
                log.warn("删除网站失败，网站不存在，id = {}", id);
                return new BusinessException(ErrorMessageEnum.SITE_NOT_EXISTS.getMessage(), ErrorMessageEnum.SITE_NOT_EXISTS.getIndex());
            });
            tbThemeEntity.setIsDelete(RecordRemoveStatusEnum._LOGICALLY_DELETED.getIndex());
            tbThemeEntityService.removeById(id);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(Integer id, String themeName, Integer themeType, String themeAuthor, String themeVersion, Integer themeThumbFileId, String themeConfig) throws BusinessException {
        TbThemeEntity tbThemeEntity = this.lockById(id).orElseThrow(() -> {
            log.warn("删除网站失败，网站不存在，id = {}", id);
            return new BusinessException(ErrorMessageEnum.SITE_NOT_EXISTS.getMessage(), ErrorMessageEnum.SITE_NOT_EXISTS.getIndex());
        });
        tbThemeEntity.setThemeName(themeName);
        tbThemeEntity.setThemeType(themeType);
        tbThemeEntity.setThemeAuthor(themeAuthor);
        tbThemeEntity.setThemeVersion(themeVersion);
        tbThemeEntity.setThemeThumbFileId(themeThumbFileId);
        tbThemeEntity.setThemeConfig(themeConfig);
        return tbThemeEntityService.updateById(tbThemeEntity);
    }


    /**
     * 锁表查
     *
     * @param id
     * @return
     */
    @Override
    public Optional<TbThemeEntity> lockById(Serializable id) {
        return Optional.ofNullable(iThemeDao.lockById(id));
    }

    /**
     * 查询单个
     *
     * @param id
     * @return
     */
    @Override
    public Optional<ThemeListResultDto> getById(Integer id) throws BusinessException {
        this.lockById(id).orElseThrow(() -> {
            log.warn("查看网站失败，网站不存在，id = {}", id);
            return new BusinessException(ErrorMessageEnum.SITE_NOT_EXISTS.getMessage(), ErrorMessageEnum.SITE_NOT_EXISTS.getIndex());
        });
        ThemeQueryConditionDto themeQueryConditionDto = new ThemeQueryConditionDto();
        themeQueryConditionDto.setId(id);
        IPage<ThemeListResultDto> themeQueryResultDtoIPage = this.page(themeQueryConditionDto, 1, 1);
        return Optional.ofNullable(themeQueryResultDtoIPage.getTotal() == 0 ? null : themeQueryResultDtoIPage.getRecords().get(0));
    }

    /**
     * 查询列表
     *
     * @param themeQueryConditionDto
     * @return
     */
    @Override
    public List<ThemeListResultDto> list(ThemeQueryConditionDto themeQueryConditionDto) {
        ThemeListConditionPo themeListConditionPo = MiscUtils.copyProperties(themeQueryConditionDto, ThemeListConditionPo.class);
        List<ThemeListResultDo> themeListResultDoList = iThemeDao.list(themeListConditionPo);
        themeListResultDoList.forEach(themeListResultDo -> {
            themeListResultDo.setThemeThumbFileVo(tbFileEntityService.getById(themeListResultDo.getThemeThumbFileId()));
        });
        return MiscUtils.copyList(themeListResultDoList, ThemeListResultDto.class);
    }

    /**
     * 列表-分页
     *
     * @param themeQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public IPage<ThemeListResultDto> page(ThemeQueryConditionDto themeQueryConditionDto, int pageNumber, int pageSize) {
        Page<ThemeListResultDo> page = PageHelper.startPage(pageNumber, pageSize);
        PageHelper.orderBy(String.format("%s desc", TbThemeEntity.ID));
        List<ThemeListResultDto> themeQueryResultDtoList = list(themeQueryConditionDto);
        IPage<ThemeListResultDto> themeQueryResultDtoIPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        themeQueryResultDtoIPage.setTotal(page.getTotal());
        themeQueryResultDtoIPage.setRecords(themeQueryResultDtoList);
        return themeQueryResultDtoIPage;
    }
}
