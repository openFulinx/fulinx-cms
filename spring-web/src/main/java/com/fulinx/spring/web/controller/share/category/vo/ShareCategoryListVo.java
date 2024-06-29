/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.share.category.vo;

import com.fulinx.spring.web.framework.base.BaseParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ShareCategoryListVo extends BaseParameterVo {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "Soft Delete Flag")
    private Integer isDelete;

}
