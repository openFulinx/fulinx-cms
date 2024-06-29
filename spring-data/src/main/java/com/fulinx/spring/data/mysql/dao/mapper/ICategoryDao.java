/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.mapper;


import com.fulinx.spring.data.mysql.dao.podo.category.CategoryListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.category.CategoryListResultDo;
import com.fulinx.spring.data.mysql.entity.TbCategoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

@Mapper
public interface ICategoryDao {

    List<CategoryListResultDo> list(CategoryListConditionPo CategoryListConditionPo);

    TbCategoryEntity lockById(Serializable id);
}
