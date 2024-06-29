/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.mapper;


import com.fulinx.spring.data.mysql.dao.podo.article.ArticleCategoryListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.article.ArticleCategoryListResultDo;
import com.fulinx.spring.data.mysql.entity.TbArticleCategoryRelationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

@Mapper
public interface IArticleCategoryDao {

    TbArticleCategoryRelationEntity lockById(Serializable id);

    List<ArticleCategoryListResultDo> list(ArticleCategoryListConditionPo articleCategoryListConditionPo);
}
