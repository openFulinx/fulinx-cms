/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.category.vo;

import com.fulinx.spring.web.framework.base.BaseParameterVo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "修改分类请求参数")
public class CategoryUpdateVo extends BaseParameterVo {

    @Schema(description = "Language Id", required = true)
    @NotNull
    private Integer languageId;

    @Schema(description = "Parent Id", required = true)
    @NotNull
    private Integer parentId;

    @Schema(description = "Category Name", required = true)
    @NotBlank
    private String categoryName;

    @Schema(description = "Category Description")
    private String categoryDescription;

    @Schema(description = "Status, 0: Disabled 1: Enabled", required = true)
    private Boolean status;

    @Schema(description = "Meta Title", required = true)
    @NotBlank
    private String metaTitle;

    @Schema(description = "Meta Description")
    private String metaDescription;

    @Schema(description = "File ID")
    private Integer fileId;
}
