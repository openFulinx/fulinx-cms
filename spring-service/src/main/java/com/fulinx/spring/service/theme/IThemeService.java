/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.theme;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.entity.TbThemeEntity;
import com.fulinx.spring.service.theme.dto.ThemeListResultDto;
import com.fulinx.spring.service.theme.dto.ThemeQueryConditionDto;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IThemeService {

    Optional<TbThemeEntity> create(String themeName, Integer themeType, String themeAuthor, String themeVersion, Integer themeThumbFileId, String themeConfig) throws BusinessException;

    /**
     * 删除文件
     *
     * @param themeIds
     * @return
     * @throws BusinessException
     */
    boolean remove(List<Integer> themeIds) throws BusinessException;


    boolean update(Integer id,String themeName, Integer themeType, String themeAuthor, String themeVersion, Integer themeThumbFileId, String themeConfig) throws BusinessException;

    /**
     * 锁表查
     *
     * @param id
     * @return
     */
    Optional<TbThemeEntity> lockById(Serializable id);

    /**
     * 查询单个
     *
     * @param id
     * @return
     */
    Optional<ThemeListResultDto> getById(Integer id) throws BusinessException;

    /**
     * 查询列表
     *
     * @param themeQueryConditionDto
     * @return
     */
    List<ThemeListResultDto> list(ThemeQueryConditionDto themeQueryConditionDto);

    /**
     * 列表-分页
     *
     * @param themeQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    IPage<ThemeListResultDto> page(ThemeQueryConditionDto themeQueryConditionDto, int pageNumber, int pageSize);

}
