/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.article.vo;

import com.fulinx.spring.web.framework.base.BaseParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "新增文章请求参数")
public class ArticleCreateVo extends BaseParameterVo {

    @Schema(description = "文章名称", required = true)
    @NotNull
    private String articleName;

    @Schema(description = "元标题", required = true)
    @NotBlank
    private String metaTitle;

    @Schema(description = "关键词")
    private String metaKeywords;

    @Schema(description = "元描述")
    private String metaDescription;

    @Schema(description = "文章描述", required = true)
    @NotBlank
    private String articleDescription;

    @Schema(description = "文件ID数组")
    private List<Integer> fileIds;

    @Schema(description = "分类ID")
    private List<Integer> categoryIds;

    @Schema(description = "文件Tag数组")
    private List<String> tags;

    @Schema(description = "浏览数量")
    private Integer viewCount;

    @Schema(description = "状态", required = true)
    private Boolean status;
}
