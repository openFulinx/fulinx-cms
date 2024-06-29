/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.mapper;


import com.fulinx.spring.data.mysql.dao.podo.article.ArticleTagListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.article.ArticleTagListResultDo;
import com.fulinx.spring.data.mysql.entity.TbArticleTagEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

@Mapper
public interface IArticleTagDao {

    List<ArticleTagListResultDo> list(ArticleTagListConditionPo articleTagListConditionPo);

    TbArticleTagEntity lockById(Serializable id);
}
