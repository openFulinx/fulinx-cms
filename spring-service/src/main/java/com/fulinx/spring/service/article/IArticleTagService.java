/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.entity.TbArticleTagEntity;
import com.fulinx.spring.service.article.dto.ArticleTagListResultDto;
import com.fulinx.spring.service.article.dto.ArticleTagQueryConditionDto;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IArticleTagService {

    /**
     * @return
     * @throws BusinessException
     */
    Optional<TbArticleTagEntity> create(Integer articleId, String tag) throws BusinessException;

    Boolean remove(List<Integer> ids) throws BusinessException;

    /**
     * 锁表查询单条记录
     *
     * @param id
     * @return
     */
    Optional<TbArticleTagEntity> lockById(Serializable id);

    /**
     * 获取单条记录
     *
     * @param id
     * @return
     */
    Optional<ArticleTagListResultDto> getById(Integer id) throws BusinessException;

    /**
     * 列表查询
     *
     * @param articleTagQueryConditionDto
     * @return
     */
    List<ArticleTagListResultDto> list(ArticleTagQueryConditionDto articleTagQueryConditionDto);

    /**
     * 列表带分页查询
     *
     * @param articleTagQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    IPage<ArticleTagListResultDto> page(ArticleTagQueryConditionDto articleTagQueryConditionDto, int pageNumber, int pageSize);

}
