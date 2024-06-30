/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.generic.ResultListVo;
import com.fulinx.spring.core.generic.ResultVo;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.entity.TbArticleEntity;
import com.fulinx.spring.service.article.IArticleService;
import com.fulinx.spring.service.article.dto.ArticleListResultDto;
import com.fulinx.spring.service.article.dto.ArticleQueryConditionDto;
import com.fulinx.spring.web.controller.serverSide.article.vo.*;
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

@Tag(name = "系统端-文章管理")
@Slf4j
@Validated
@RestController
@RequestMapping("/server-side/article")
public class ArticleController extends BaseServerSideController {

    private final IArticleService iArticleService;

    public ArticleController(IArticleService iArticleService) {
        this.iArticleService = iArticleService;
    }

    /**
     * 新增文章
     *
     * @param articleCreateVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "新增文章", method = "POST")
    @PostMapping
    public ResultVo<Optional<TbArticleEntity>> CreateArticle(@RequestBody @Valid ArticleCreateVo articleCreateVo) throws BusinessException {
        Integer systemUserId = getSystemUserId();
        return ResultVo.build(iArticleService.create(articleCreateVo.getArticleName(), articleCreateVo.getArticleDescription(), articleCreateVo.getMetaTitle(),  articleCreateVo.getMetaDescription(), articleCreateVo.getFileIds(), articleCreateVo.getCategoryIds(), articleCreateVo.getTags(), articleCreateVo.getStatus()));
    }


    /**
     * 删除文章
     *
     * @param articleDeleteVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "删除文章", method = "DELETE")
    @DeleteMapping
    public ResultVo<Boolean> RemoveArticle(@RequestBody ArticleDeleteVo articleDeleteVo) throws BusinessException {
        return ResultVo.build(iArticleService.remove(articleDeleteVo.getIds()));
    }

    /**
     * 更新文章
     *
     * @param id
     * @param articleUpdateVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "更新文章", method = "PUT")
    @PutMapping("/{id}")
    public ResultVo<TbArticleEntity> UpdateArticle(@PathVariable(value = "id") @Valid @NotNull @Min(1) Integer id, @RequestBody @Valid ArticleUpdateVo articleUpdateVo) throws BusinessException {
        return ResultVo.build(iArticleService.update(id, articleUpdateVo.getArticleName(), articleUpdateVo.getArticleDescription(), articleUpdateVo.getMetaTitle(),  articleUpdateVo.getMetaDescription(), articleUpdateVo.getFileIds(), articleUpdateVo.getDeletedFileIds(), articleUpdateVo.getCategoryIds(), articleUpdateVo.getDeletedCategoryIds(), articleUpdateVo.getTags(), articleUpdateVo.getDeletedTagIds(), articleUpdateVo.getStatus()));
    }

    /**
     * 查看文章
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "查看文章", method = "GET")
    @GetMapping("/{id}")
    public ResultVo<Optional<ArticleListResultDto>> ShowArticle(@PathVariable(value = "id") @Valid @NonNull @Min(1) Integer id) throws BusinessException {
        return ResultVo.build(iArticleService.getById(id));
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
    public ResultVo<ResultListVo<ArticleListResultDto>> PaginationAll(@RequestBody @Valid ArticlePaginationParameterVo articlePaginationParameterVo) throws BusinessException {
        ArticleQueryConditionDto articleQueryConditionDto = MiscUtils.copyProperties(articlePaginationParameterVo, ArticleQueryConditionDto.class);
        IPage<ArticleListResultDto> articleListResultDoIPage = iArticleService.page(articleQueryConditionDto, articlePaginationParameterVo.getPageNumber(), articlePaginationParameterVo.getPageSize());
        return ResultVo.build(ResultListVo.build(articleListResultDoIPage.getRecords(), articleListResultDoIPage.getTotal()));
    }


    /**
     * 列表-不带分页
     *
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "文章列表 - 不带分页", method = "GET")
    @PostMapping("/list")
    public ResultVo<ResultListVo<ArticleListResultDto>> List(@RequestBody(required = false) @Valid ArticleListVo articleListVo) throws BusinessException {
        if (articleListVo != null) {
            ArticleQueryConditionDto articleQueryConditionDto = MiscUtils.copyProperties(articleListVo, ArticleQueryConditionDto.class);
            List<ArticleListResultDto> list = iArticleService.list(articleQueryConditionDto);
            return ResultVo.build(ResultListVo.build(list, list.size()));
        } else {
            List<ArticleListResultDto> list = iArticleService.list(new ArticleQueryConditionDto());
            return ResultVo.build(ResultListVo.build(list, list.size()));
        }
    }
}
