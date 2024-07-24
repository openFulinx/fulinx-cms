/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.site;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.generic.ResultListVo;
import com.fulinx.spring.core.generic.ResultVo;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.entity.TbSiteEntity;
import com.fulinx.spring.data.mysql.entity.TbSiteEntity;
import com.fulinx.spring.service.site.ISiteService;
import com.fulinx.spring.service.site.dto.SiteListResultDto;
import com.fulinx.spring.service.site.dto.SiteQueryConditionDto;
import com.fulinx.spring.service.enums.ErrorMessageEnum;
import com.fulinx.spring.service.site.ISiteService;
import com.fulinx.spring.service.site.dto.SiteListResultDto;
import com.fulinx.spring.service.site.dto.SiteQueryConditionDto;
import com.fulinx.spring.web.controller.serverSide.site.vo.*;
import com.fulinx.spring.web.controller.serverSide.site.vo.SiteDeleteVo;
import com.fulinx.spring.web.controller.serverSide.site.vo.SitePaginationParameterVo;
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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "系统端-网站")
@RestController
@Slf4j
@Validated
@RequestMapping("/server-side/site")
public class SiteController extends BaseServerSideController {

    private final ISiteService iSiteService;

    public SiteController(ISiteService iSiteService) {
        this.iSiteService = iSiteService;
    }


    @Operation(summary = "新增网站", method = "POST")
    @PostMapping
    public ResultVo<Optional<TbSiteEntity>> Create(@RequestBody @Valid SiteCreateVo siteCreateVo) throws BusinessException {
        return ResultVo.build(iSiteService.create(siteCreateVo.getThemeId(), siteCreateVo.getDomain(), siteCreateVo.getLanguageId(), siteCreateVo.getSiteName(), siteCreateVo.getMetaTitle(), siteCreateVo.getMetaDescription(), siteCreateVo.getLogoFileId(), siteCreateVo.getFaviconFileId(), siteCreateVo.getStatus()));
    }


    /**
     * 删除网站
     *
     * @param siteDeleteVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "删除网站", method = "DELETE")
    @DeleteMapping
    public ResultVo<Boolean> RemoveSite(@RequestBody SiteDeleteVo siteDeleteVo) throws BusinessException {
        return ResultVo.build(iSiteService.remove(siteDeleteVo.getIds()));
    }

    /**
     * 更新网站
     *
     * @param id
     * @param siteUpdateVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "更新网站", method = "PUT")
    @PutMapping("/{id}")
    public ResultVo<Boolean> Update(@PathVariable(value = "id") @Valid @NotNull @Min(1) Integer id, @RequestBody @Valid SiteUpdateVo siteUpdateVo) throws BusinessException {
        return ResultVo.build(iSiteService.update(id, siteUpdateVo.getThemeId(), siteUpdateVo.getDomain(), siteUpdateVo.getLanguageId(), siteUpdateVo.getSiteName(), siteUpdateVo.getMetaTitle(), siteUpdateVo.getMetaDescription(), siteUpdateVo.getLogoFileId(), siteUpdateVo.getFaviconFileId(), siteUpdateVo.getStatus()));
    }

    /**
     * 查看网站
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "查看网站", method = "GET")
    @GetMapping("/{id}")
    public ResultVo<Optional<SiteListResultDto>> Show(@PathVariable(value = "id") @Valid @NonNull @Min(1) Integer id) throws BusinessException {
        return ResultVo.build(iSiteService.getById(id));
    }

    /**
     * 全部作品列表-带分页
     *
     * @param sitePaginationParameterVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "全部网站列表", method = "POST")
    @PostMapping("/pagination")
    public ResultVo<ResultListVo<SiteListResultDto>> Pagination(@RequestBody @Valid SitePaginationParameterVo sitePaginationParameterVo) throws BusinessException {
        SiteQueryConditionDto siteQueryConditionDto = MiscUtils.copyProperties(sitePaginationParameterVo, SiteQueryConditionDto.class);
        IPage<SiteListResultDto> siteListResultDoIPage = iSiteService.page(siteQueryConditionDto, sitePaginationParameterVo.getPageNumber(), sitePaginationParameterVo.getPageSize());
        return ResultVo.build(ResultListVo.build(siteListResultDoIPage.getRecords(), siteListResultDoIPage.getTotal()));
    }


    /**
     * 列表-不带分页
     *
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "网站列表 - 不带分页", method = "GET")
    @PostMapping("/list")
    public ResultVo<ResultListVo<SiteListResultDto>> List(@RequestBody(required = false) @Valid SiteListVo siteListVo) throws BusinessException {
        if (siteListVo != null) {
            SiteQueryConditionDto siteQueryConditionDto = MiscUtils.copyProperties(siteListVo, SiteQueryConditionDto.class);
            List<SiteListResultDto> list = iSiteService.list(siteQueryConditionDto);
            return ResultVo.build(ResultListVo.build(list, list.size()));
        } else {
            List<SiteListResultDto> list = iSiteService.list(new SiteQueryConditionDto());
            return ResultVo.build(ResultListVo.build(list, list.size()));
        }
    }
}
