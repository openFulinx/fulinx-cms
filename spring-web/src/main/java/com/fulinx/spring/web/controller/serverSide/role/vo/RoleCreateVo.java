/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.role.vo;

import com.fulinx.spring.web.framework.base.BaseParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoleCreateVo extends BaseParameterVo {

    @Schema(description = "角色名称")
    @NotBlank
    private String roleName;

    @Schema(description = "权限ID列表")
    @NotEmpty
    private List<Integer> permissionIds;
}
