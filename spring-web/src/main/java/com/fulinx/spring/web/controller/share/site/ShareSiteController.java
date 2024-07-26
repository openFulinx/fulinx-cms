/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.share.site;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.generic.ResultListVo;
import com.fulinx.spring.core.generic.ResultVo;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.entity.TbSiteEntity;
import com.fulinx.spring.data.mysql.service.TbSiteEntityService;
import com.fulinx.spring.service.site.ISiteService;
import com.fulinx.spring.service.site.dto.SiteListResultDto;
import com.fulinx.spring.service.site.dto.SiteQueryConditionDto;
import com.fulinx.spring.web.controller.serverSide.site.vo.*;
import com.fulinx.spring.web.controller.share.site.vo.ShareSiteListVo;
import com.fulinx.spring.web.controller.share.site.vo.ShareSiteShowVo;
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

@Tag(name = "公开接口-网站")
@RestController
@Slf4j
@Validated
@RequestMapping("/share/public/site")
public class ShareSiteController extends BaseServerSideController {

    private final ISiteService iSiteService;

    public ShareSiteController(ISiteService iSiteService) {
        this.iSiteService = iSiteService;
    }

    /**
     * 查看网站
     *
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "查看网站", method = "GET")
    @PostMapping()
    public ResultVo<Optional<SiteListResultDto>> Show(@RequestBody @Valid ShareSiteShowVo shareSiteShowVo) throws BusinessException {
        return ResultVo.build(iSiteService.getByDomain(shareSiteShowVo.getDomain()));
    }

    /**
     * 列表-不带分页
     *
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "网站列表 - 不带分页", method = "GET")
    @PostMapping("/list")
    public ResultVo<ResultListVo<SiteListResultDto>> List(@RequestBody(required = false) @Valid ShareSiteListVo shareSiteListVo) throws BusinessException {
        if (shareSiteListVo != null) {
            SiteQueryConditionDto siteQueryConditionDto = MiscUtils.copyProperties(shareSiteListVo, SiteQueryConditionDto.class);
            List<SiteListResultDto> list = iSiteService.list(siteQueryConditionDto);
            return ResultVo.build(ResultListVo.build(list, list.size()));
        } else {
            List<SiteListResultDto> list = iSiteService.list(new SiteQueryConditionDto());
            return ResultVo.build(ResultListVo.build(list, list.size()));
        }
    }
}
