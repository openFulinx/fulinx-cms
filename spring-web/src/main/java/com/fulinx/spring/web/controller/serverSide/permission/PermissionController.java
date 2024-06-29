/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.permission;

import com.fulinx.spring.core.generic.ResultListVo;
import com.fulinx.spring.core.generic.ResultVo;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.service.permission.IPermissionService;
import com.fulinx.spring.service.permission.dto.PermissionListResultDto;
import com.fulinx.spring.service.permission.dto.PermissionQueryConditionDto;
import com.fulinx.spring.web.framework.base.BaseServerSideController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "系统端-角色")
@RestController
@Slf4j
@Validated
@RequestMapping("/system/permission")
public class PermissionController extends BaseServerSideController {


    private IPermissionService iPermissionService;

    public PermissionController(IPermissionService iPermissionService) {
        this.iPermissionService = iPermissionService;
    }


    /**
     * 列表-不带分页
     *
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "权限列表 - 不带分页", method = "GET")
    @GetMapping("/list")
    public ResultVo<ResultListVo<PermissionListResultDto>> List() throws BusinessException {
        List<PermissionListResultDto> list = iPermissionService.list(new PermissionQueryConditionDto());
        return ResultVo.build(ResultListVo.build(list, list.size() > 0 ? list.size() : 0));
    }
}
