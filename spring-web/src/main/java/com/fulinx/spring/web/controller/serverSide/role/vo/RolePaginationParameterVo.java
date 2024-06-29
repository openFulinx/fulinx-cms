/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.role.vo;

import com.fulinx.spring.web.framework.base.BasePaginationParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "角色分页查询参数")
public class RolePaginationParameterVo extends BasePaginationParameterVo {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "Soft Delete Flag")
    private Integer isDelete;

}
