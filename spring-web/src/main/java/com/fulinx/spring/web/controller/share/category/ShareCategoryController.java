/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.share.category;

import com.fulinx.spring.core.generic.ResultListVo;
import com.fulinx.spring.core.generic.ResultVo;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.service.category.ICategoryService;
import com.fulinx.spring.service.category.dto.CategoryArticleListResultDto;
import com.fulinx.spring.service.category.dto.CategoryListResultDto;
import com.fulinx.spring.service.category.dto.CategoryQueryConditionDto;
import com.fulinx.spring.web.controller.share.category.vo.ShareCategoryListVo;
import com.fulinx.spring.web.framework.base.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "公开接口-分类")
@Slf4j
@Validated
@RestController
@RequestMapping("/share/public/categories")
public class ShareCategoryController extends BaseController {

    private final ICategoryService iCategoryService;

    public ShareCategoryController(ICategoryService iCategoryService) {
        this.iCategoryService = iCategoryService;
    }

    /**
     * 查看分类
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "查看分类", method = "GET")
    @GetMapping("/{id}")
    public ResultVo<Optional<CategoryListResultDto>> ShowCategory(@PathVariable(value = "id") @Valid @NotNull @Min(1) Integer id) throws BusinessException {
        return ResultVo.build(iCategoryService.getById(id));
    }

    /**
     * 列表-不带分页
     *
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "分类列表 - 不带分页", method = "POST")
    @PostMapping("/list")
    public ResultVo<ResultListVo<CategoryListResultDto>> ListCategory(@RequestBody(required = false) @Valid ShareCategoryListVo shareCategoryListVo) throws BusinessException {
        if (shareCategoryListVo != null) {
            CategoryQueryConditionDto categoryQueryConditionDto = MiscUtils.copyProperties(shareCategoryListVo, CategoryQueryConditionDto.class);
            List<CategoryListResultDto> list = iCategoryService.list(categoryQueryConditionDto);
            return ResultVo.build(ResultListVo.build(list, list.size()));
        } else {
            List<CategoryListResultDto> list = iCategoryService.list(new CategoryQueryConditionDto());
            return ResultVo.build(ResultListVo.build(list, list.size()));
        }
    }

    /**
     * 首页-分类-文章
     *
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "首页分类文章列表 - 不带分页", method = "GET")
    @GetMapping("/home/article/list")
    public ResultVo<ResultListVo<CategoryArticleListResultDto>> ListHomeCategoryArticles() throws BusinessException {
        List<CategoryArticleListResultDto> list = iCategoryService.listCategoryArticle();
        return ResultVo.build(ResultListVo.build(list, list.size()));
    }
}
