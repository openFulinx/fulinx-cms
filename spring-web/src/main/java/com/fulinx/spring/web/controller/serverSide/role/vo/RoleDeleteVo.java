/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.role.vo;

import com.fulinx.spring.web.framework.base.BaseParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoleDeleteVo extends BaseParameterVo {
    @Schema(description = "角色ID数组")
    @NotEmpty
    private List<Integer> ids;
}
