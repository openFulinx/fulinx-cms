/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.role;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.generic.ResultListVo;
import com.fulinx.spring.core.generic.ResultVo;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.service.role.IRoleOperateService;
import com.fulinx.spring.service.role.IRoleService;
import com.fulinx.spring.service.role.dto.RoleListResultDto;
import com.fulinx.spring.service.role.dto.RoleOneResultDto;
import com.fulinx.spring.service.role.dto.RoleQueryConditionDto;
import com.fulinx.spring.web.controller.serverSide.role.vo.*;
import com.fulinx.spring.web.framework.base.BaseServerSideController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "系统端-角色")
@RestController
@Slf4j
@Validated
@RequestMapping("/system/role")
public class RoleController extends BaseServerSideController {

    private IRoleOperateService iRoleOperateService;

    private IRoleService iRoleService;

    public RoleController(IRoleOperateService iRoleOperateService, IRoleService iRoleService) {
        this.iRoleOperateService = iRoleOperateService;
        this.iRoleService = iRoleService;
    }

    /**
     * 新增角色
     *
     * @param roleCreateVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "新增角色", method = "POST")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('sys:roles','sys:roles:add')")
    public ResultVo<Integer> Create(@RequestBody @Valid RoleCreateVo roleCreateVo) throws BusinessException {
        return ResultVo.build(iRoleOperateService.create(roleCreateVo.getRoleName(), roleCreateVo.getPermissionIds()));
    }

    /**
     * 删除角色
     *
     * @param roleDeleteVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "删除角色", method = "DELETE")
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('sys:roles','sys:roles:remove')")
    public ResultVo<Boolean> Remove(@RequestBody RoleDeleteVo roleDeleteVo) throws BusinessException {
        return ResultVo.build(iRoleOperateService.remove(roleDeleteVo.getIds()));
    }

    /**
     * 更新角色
     *
     * @param id
     * @param roleUpdateVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "更新角色", method = "PUT")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('sys:roles','sys:roles:update')")
    public ResultVo<Boolean> Update(@PathVariable(value = "id") @Valid @NotNull @Min(1) Integer id, @RequestBody @Valid RoleUpdateVo roleUpdateVo) throws BusinessException {
        return ResultVo.build(iRoleOperateService.update(id, roleUpdateVo.getRoleName(), roleUpdateVo.getPermissionIds(), roleUpdateVo.getDeletedPermissionIds()));
    }

    /**
     * 查看角色
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "查看角色", method = "GET")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('sys:roles','sys:roles:show')")
    public ResultVo<Optional<RoleOneResultDto>> Show(@PathVariable(value = "id") @Valid @NonNull @Min(1) Integer id) throws BusinessException {
        return ResultVo.build(iRoleOperateService.getById(id));
    }

    /**
     * 列表-带分页
     *
     * @param rolePaginationParameterVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "角色列表 - 带分页", method = "POST")
    @PostMapping("/pagination")
    @PreAuthorize("hasAnyAuthority('sys:roles','sys:roles:pagination')")
    public ResultVo<ResultListVo<RoleListResultDto>> Pagination(@RequestBody @Valid RolePaginationParameterVo rolePaginationParameterVo) throws BusinessException {
        RoleQueryConditionDto roleQueryConditionDto = MiscUtils.copyProperties(rolePaginationParameterVo, RoleQueryConditionDto.class);
        IPage<RoleListResultDto> roleListResultDoIPage = iRoleService.page(roleQueryConditionDto, rolePaginationParameterVo.getPageNumber(), rolePaginationParameterVo.getPageSize());
        return ResultVo.build(ResultListVo.build(roleListResultDoIPage.getRecords(), roleListResultDoIPage.getTotal()));
    }

    /**
     * 列表-不带分页
     *
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "角色列表 - 不带分页", method = "GET")
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('sys:roles','sys:roles:list')")
    public ResultVo<ResultListVo<RoleListResultDto>> List(@RequestBody(required = false) @Valid RoleListVo roleListVo) throws BusinessException {
        if(roleListVo != null){
            RoleQueryConditionDto roleQueryConditionDto = MiscUtils.copyProperties(roleListVo, RoleQueryConditionDto.class);
            List<RoleListResultDto> list = iRoleService.list(roleQueryConditionDto);
            return ResultVo.build(ResultListVo.build(list, list.size()));
        } else {
            List<RoleListResultDto> list = iRoleService.list(new RoleQueryConditionDto());
            return ResultVo.build(ResultListVo.build(list, list.size()));
        }
    }
}
