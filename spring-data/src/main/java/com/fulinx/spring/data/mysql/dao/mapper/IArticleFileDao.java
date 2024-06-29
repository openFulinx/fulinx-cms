/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.mapper;


import com.fulinx.spring.data.mysql.dao.podo.article.ArticleFileListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.article.ArticleFileListResultDo;
import com.fulinx.spring.data.mysql.entity.TbArticleFileRelationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

@Mapper
public interface IArticleFileDao {

    TbArticleFileRelationEntity lockById(Serializable id);

    List<ArticleFileListResultDo> list(ArticleFileListConditionPo articleFileListConditionPo);
}
