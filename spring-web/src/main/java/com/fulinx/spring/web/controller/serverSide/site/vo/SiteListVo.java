/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.site.vo;

import com.fulinx.spring.web.framework.base.BaseParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "分类列表请求参数")
public class SiteListVo extends BaseParameterVo {

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "文章ID")
    private Integer articleId;

    @Schema(description = "Category Name")
    private String categoryName;

    @Schema(description = "Status, 0: Disabled 1: Enabled")
    private Boolean status;

    @Schema(description = "删除标识")
    private Integer isDelete;
}
