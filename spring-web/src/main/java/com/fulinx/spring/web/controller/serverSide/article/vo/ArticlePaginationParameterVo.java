/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.article.vo;

import com.fulinx.spring.web.framework.base.BasePaginationParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "分类分页查询参数")
public class ArticlePaginationParameterVo extends BasePaginationParameterVo {

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "Article Id")
    private Integer articleId;

    @Schema(description = "文章标题")
    private String articleName;

    @Schema(description = "文章分类")
    private Integer categoryId;

    @Schema(description = "Status, 0: Disabled 1: Enabled")
    private Boolean status;

    @Schema(description = "删除标识")
    private Integer isDelete;

}
