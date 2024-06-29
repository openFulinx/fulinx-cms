/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.mapper;


import com.fulinx.spring.data.mysql.entity.TbCategoryDetailEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;

@Mapper
public interface ICategoryDescriptionDao {

    TbCategoryDetailEntity lockByCategoryId(Serializable id);
}
