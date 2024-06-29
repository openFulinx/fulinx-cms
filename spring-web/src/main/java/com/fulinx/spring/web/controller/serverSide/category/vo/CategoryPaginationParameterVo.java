/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.category.vo;

import com.fulinx.spring.web.framework.base.BasePaginationParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "分类分页查询参数")
public class CategoryPaginationParameterVo extends BasePaginationParameterVo {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "Category Name")
    private String categoryName;

    @Schema(description = "分类类型")
    private String categoryType;

    @Schema(description = "Status, 0: Disabled 1: Enabled")
    private Boolean status;

    @Schema(description = "删除标识")
    private Integer isDelete;

}
