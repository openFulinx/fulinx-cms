/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.theme;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.generic.ResultListVo;
import com.fulinx.spring.core.generic.ResultVo;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.entity.TbThemeEntity;
import com.fulinx.spring.service.theme.IThemeService;
import com.fulinx.spring.service.theme.dto.ThemeListResultDto;
import com.fulinx.spring.service.theme.dto.ThemeQueryConditionDto;
import com.fulinx.spring.web.controller.serverSide.theme.vo.*;
import com.fulinx.spring.web.framework.base.BaseServerSideController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "系统端-主题")
@RestController
@Slf4j
@Validated
@RequestMapping("/server-side/theme")
public class ThemeController extends BaseServerSideController {

    private final IThemeService iThemeService;

    public ThemeController(IThemeService iThemeService) {
        this.iThemeService = iThemeService;
    }


    @Operation(summary = "新增主题", method = "POST")
    @PostMapping
    public ResultVo<Optional<TbThemeEntity>> Create(@RequestBody @Valid ThemeCreateVo themeCreateVo) throws BusinessException {
        return ResultVo.build(iThemeService.create(themeCreateVo.getThemeName(), themeCreateVo.getThemeType(), themeCreateVo.getThemeAuthor(), themeCreateVo.getThemeVersion(), themeCreateVo.getThemeThumbFileId(), themeCreateVo.getThemeConfig()));
    }


    /**
     * 删除主题
     *
     * @param themeDeleteVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "删除主题", method = "DELETE")
    @DeleteMapping
    public ResultVo<Boolean> RemoveTheme(@RequestBody ThemeDeleteVo themeDeleteVo) throws BusinessException {
        return ResultVo.build(iThemeService.remove(themeDeleteVo.getIds()));
    }

    /**
     * 更新主题
     *
     * @param id
     * @param themeUpdateVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "更新主题", method = "PUT")
    @PutMapping("/{id}")
    public ResultVo<Boolean> Update(@PathVariable(value = "id") @Valid @NotNull @Min(1) Integer id, @RequestBody @Valid ThemeUpdateVo themeUpdateVo) throws BusinessException {
        return ResultVo.build(iThemeService.update(id, themeUpdateVo.getThemeName(), themeUpdateVo.getThemeType(), themeUpdateVo.getThemeAuthor(), themeUpdateVo.getThemeVersion(), themeUpdateVo.getThemeThumbFileId(), themeUpdateVo.getThemeConfig()));
    }

    /**
     * 查看主题
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "查看主题", method = "GET")
    @GetMapping("/{id}")
    public ResultVo<Optional<ThemeListResultDto>> Show(@PathVariable(value = "id") @Valid @NonNull @Min(1) Integer id) throws BusinessException {
        return ResultVo.build(iThemeService.getById(id));
    }

    /**
     * 全部作品列表-带分页
     *
     * @param themePaginationParameterVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "全部主题列表", method = "POST")
    @PostMapping("/pagination")
    public ResultVo<ResultListVo<ThemeListResultDto>> Pagination(@RequestBody @Valid ThemePaginationParameterVo themePaginationParameterVo) throws BusinessException {
        ThemeQueryConditionDto themeQueryConditionDto = MiscUtils.copyProperties(themePaginationParameterVo, ThemeQueryConditionDto.class);
        IPage<ThemeListResultDto> themeListResultDoIPage = iThemeService.page(themeQueryConditionDto, themePaginationParameterVo.getPageNumber(), themePaginationParameterVo.getPageSize());
        return ResultVo.build(ResultListVo.build(themeListResultDoIPage.getRecords(), themeListResultDoIPage.getTotal()));
    }


    /**
     * 列表-不带分页
     *
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "主题列表 - 不带分页", method = "GET")
    @PostMapping("/list")
    public ResultVo<ResultListVo<ThemeListResultDto>> List(@RequestBody(required = false) @Valid ThemeListVo themeListVo) throws BusinessException {
        if (themeListVo != null) {
            ThemeQueryConditionDto themeQueryConditionDto = MiscUtils.copyProperties(themeListVo, ThemeQueryConditionDto.class);
            List<ThemeListResultDto> list = iThemeService.list(themeQueryConditionDto);
            return ResultVo.build(ResultListVo.build(list, list.size()));
        } else {
            List<ThemeListResultDto> list = iThemeService.list(new ThemeQueryConditionDto());
            return ResultVo.build(ResultListVo.build(list, list.size()));
        }
    }
}
