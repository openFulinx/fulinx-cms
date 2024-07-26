/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.share.site.vo;

import com.fulinx.spring.web.framework.base.BaseParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "网站列表请求参数")
public class ShareSiteListVo extends BaseParameterVo {

    @Schema(description = "Site ID")
    private Integer siteId;

    @Schema(description = "Domain")
    private String domain;

    @Schema(description = "Status, 0: Disabled 1: Enabled")
    private Boolean status;

    @Schema(description = "删除标识")
    private Integer isDelete;
}
