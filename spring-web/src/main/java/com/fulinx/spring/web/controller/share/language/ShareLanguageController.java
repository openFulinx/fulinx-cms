/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.share.language;

import com.fulinx.spring.core.generic.ResultListVo;
import com.fulinx.spring.core.generic.ResultVo;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.enums.LanguageEnum;
import com.fulinx.spring.web.controller.share.language.vo.ShareLanguageListVo;
import com.fulinx.spring.web.framework.base.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "公开接口-语言列表")
@Slf4j
@Validated
@RestController
@RequestMapping("/share/public/language")
public class ShareLanguageController extends BaseController {

    /**
     * 列表-不带分页
     *
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "分类列表 - 不带分页", method = "POST")
    @PostMapping("/list")
    public ResultVo<ResultListVo<?>> ListCategory(@RequestBody(required = false) @Valid ShareLanguageListVo shareLanguageListVo) throws BusinessException {
        List<Map<String, Object>> languageInfoList = LanguageEnum.getLanguageInfoList(shareLanguageListVo.getLanguageCode());
        return ResultVo.build(ResultListVo.build(languageInfoList, languageInfoList.size()));
    }
}
