/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.entity.TbArticleFileRelationEntity;
import com.fulinx.spring.service.article.dto.ArticleFileListResultDto;
import com.fulinx.spring.service.article.dto.ArticleFileQueryConditionDto;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IArticleFileService {

    /**
     * @return
     * @throws BusinessException
     */
    Optional<TbArticleFileRelationEntity> create(Integer articleId, Integer fileId) throws BusinessException;

    Boolean remove(List<Integer> ids) throws BusinessException;


    /**
     * 锁表查询单条记录
     *
     * @param id
     * @return
     */
    Optional<TbArticleFileRelationEntity> lockById(Serializable id);

    /**
     * 获取单条记录
     *
     * @param id
     * @return
     */
    Optional<ArticleFileListResultDto> getById(Integer id) throws BusinessException;

    /**
     * 列表查询
     *
     * @param articleFileQueryConditionDto
     * @return
     */
    List<ArticleFileListResultDto> list(ArticleFileQueryConditionDto articleFileQueryConditionDto);

    /**
     * 列表带分页查询
     *
     * @param articleFileQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    IPage<ArticleFileListResultDto> page(ArticleFileQueryConditionDto articleFileQueryConditionDto, int pageNumber, int pageSize);

}
