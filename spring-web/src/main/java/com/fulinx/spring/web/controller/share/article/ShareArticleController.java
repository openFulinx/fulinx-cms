/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.share.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.generic.ResultListVo;
import com.fulinx.spring.core.generic.ResultVo;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.enums.ArticleTypeEnum;
import com.fulinx.spring.data.mysql.enums.LanguageEnum;
import com.fulinx.spring.service.article.IArticleService;
import com.fulinx.spring.service.article.dto.ArticleListResultDto;
import com.fulinx.spring.service.article.dto.ArticleQueryConditionDto;
import com.fulinx.spring.web.controller.serverSide.article.vo.ArticlePaginationParameterVo;
import com.fulinx.spring.web.controller.share.article.vo.ShareArticleTypeListVo;
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

@Tag(name = "公开接口-文章")
@Slf4j
@Validated
@RestController
@RequestMapping("/share/public/article")
public class ShareArticleController extends BaseController {

    private final IArticleService iArticleService;

    public ShareArticleController(IArticleService iArticleService) {
        this.iArticleService = iArticleService;
    }

    /**
     * 列表-不带分页
     *
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "文章类型列表 - 不带分页", method = "POST")
    @PostMapping("/type/list")
    public ResultVo<ResultListVo<?>> ListArticleType(@RequestBody(required = false) @Valid ShareArticleTypeListVo shareArticleTypeListVo) throws BusinessException {
        List<Map<String, Object>> articleTypeList = ArticleTypeEnum.getLanguageInfoList(shareArticleTypeListVo.getArticleTypeCode());
        return ResultVo.build(ResultListVo.build(articleTypeList, articleTypeList.size()));
    }

    /**
     * 全部作品列表-带分页
     *
     * @param articlePaginationParameterVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "全部文章列表", method = "POST")
    @PostMapping("/pagination")
    public ResultVo<ResultListVo<ArticleListResultDto>> Pagination(@RequestBody @Valid ArticlePaginationParameterVo articlePaginationParameterVo) throws BusinessException {
        ArticleQueryConditionDto articleQueryConditionDto = MiscUtils.copyProperties(articlePaginationParameterVo, ArticleQueryConditionDto.class);
        IPage<ArticleListResultDto> articleListResultDoIPage = iArticleService.page(articleQueryConditionDto, articlePaginationParameterVo.getPageNumber(), articlePaginationParameterVo.getPageSize());
        return ResultVo.build(ResultListVo.build(articleListResultDoIPage.getRecords(), articleListResultDoIPage.getTotal()));
    }
}
