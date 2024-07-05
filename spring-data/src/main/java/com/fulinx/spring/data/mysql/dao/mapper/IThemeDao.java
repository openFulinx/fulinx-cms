/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.mapper;

import com.fulinx.spring.data.mysql.dao.podo.theme.ThemeListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.theme.ThemeListResultDo;
import com.fulinx.spring.data.mysql.entity.TbThemeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Mapper
public interface IThemeDao {

    List<ThemeListResultDo> list(ThemeListConditionPo themeListConditionPo);

    TbThemeEntity lockById(Serializable id);

    List<TbThemeEntity> lockByIds(Collection<? extends Serializable> idList);
}
