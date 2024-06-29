package com.fulinx.spring.data.mysql.dao.mapper;

import com.fulinx.spring.data.mysql.dao.podo.article.ArticleListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.article.ArticleListResultDo;
import com.fulinx.spring.data.mysql.entity.TbArticleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Mapper
public interface IArticleDao {

    List<ArticleListResultDo> list(ArticleListConditionPo articleListConditionPo);

    TbArticleEntity lockById(Serializable id);

    List<TbArticleEntity> lockByIds(Collection<? extends Serializable> idList);

    List<ArticleListResultDo> listByCategoryId(Integer categoryId);
}
