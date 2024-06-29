/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.category.dto;

import com.fulinx.spring.data.mysql.entity.TbFileEntity;
import com.fulinx.spring.service.article.dto.ArticleListResultDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class CategoryArticleListResultDto {
    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "是否为首页分类")
    private Boolean isHomeCategory;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "链接地址")
    private String linkUrl;

    @Schema(description = "文件")
    private TbFileEntity categoryFileVo;

    @Schema(description = "文章列表")
    private List<ArticleListResultDto> articles;
}
