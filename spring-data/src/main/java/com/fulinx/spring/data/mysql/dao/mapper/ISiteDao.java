/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.mapper;

import com.fulinx.spring.data.mysql.dao.podo.site.SiteListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.site.SiteListResultDo;
import com.fulinx.spring.data.mysql.entity.TbSiteEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Mapper
public interface ISiteDao {

    List<SiteListResultDo> list(SiteListConditionPo siteListConditionPo);

    TbSiteEntity lockById(Serializable id);

    List<TbSiteEntity> lockByIds(Collection<? extends Serializable> idList);
}
