/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.entity.TbArticleEntity;
import com.fulinx.spring.service.article.dto.ArticleListResultDto;
import com.fulinx.spring.service.article.dto.ArticleQueryConditionDto;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IArticleService {

    /**
     * @param articleName
     * @param articleDescription
     * @param metaTitle
     * @param metaDescription
     * @param fileIds
     * @param status
     * @return
     * @throws BusinessException
     */
    Optional<TbArticleEntity> create(String articleName, String articleDescription, String metaTitle, String metaDescription, List<Integer> fileIds, List<Integer> categoryIds, List<String> tags, Boolean status) throws BusinessException;


    Boolean remove(List<Integer> ids) throws BusinessException;

    TbArticleEntity update(Integer id, String articleName, String articleDescription, String metaTitle, String metaDescription, List<Integer> fileIds, List<Integer> deletedArticleFileIds, List<Integer> categoryIds, List<Integer> deletedCategoryIds, List<String> tags, List<Integer> deletedTagIds, Boolean status) throws BusinessException;


    /**
     * 锁表查询单条记录
     *
     * @param id
     * @return
     */
    Optional<TbArticleEntity> lockById(Serializable id);

    List<TbArticleEntity> lockByIds(List<Integer> ids);

    /**
     * 获取单条记录 - 不带登录态
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    Optional<ArticleListResultDto> getById(Integer id) throws BusinessException;

    /**
     * @param articleQueryConditionDto
     * @return
     */
    List<ArticleListResultDto> list(ArticleQueryConditionDto articleQueryConditionDto);

    /**
     * @param articleQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    IPage<ArticleListResultDto> page(ArticleQueryConditionDto articleQueryConditionDto, int pageNumber, int pageSize);


}
