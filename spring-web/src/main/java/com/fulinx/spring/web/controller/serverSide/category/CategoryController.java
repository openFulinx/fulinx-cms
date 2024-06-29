/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.category;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.generic.ResultListVo;
import com.fulinx.spring.core.generic.ResultVo;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.service.category.ICategoryService;
import com.fulinx.spring.service.category.dto.CategoryListResultDto;
import com.fulinx.spring.service.category.dto.CategoryQueryConditionDto;
import com.fulinx.spring.web.controller.serverSide.category.vo.*;
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

@Tag(name = "系统端-分类")
@RestController
@Slf4j
@Validated
@RequestMapping("/system/categories")
public class CategoryController extends BaseServerSideController {

    private ICategoryService iCategoryService;

    public CategoryController(ICategoryService iCategoryService) {
        this.iCategoryService = iCategoryService;
    }

    /**
     * 新增分类
     *
     * @param categoryCreateVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "新增分类", method = "POST")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('sys:categories','sys:categories:add')")
    public ResultVo<Optional<CategoryListResultDto>> CreateCategory(@RequestBody @Valid CategoryCreateVo categoryCreateVo) throws BusinessException {
        return ResultVo.build(iCategoryService.create(categoryCreateVo.getParentId(), categoryCreateVo.getCategoryName(), categoryCreateVo.getCategoryDescription(), categoryCreateVo.getMetaTitle(),  categoryCreateVo.getMetaDescription(), categoryCreateVo.getFileId(), categoryCreateVo.getStatus()));
    }

    /**
     * 删除分类
     *
     * @param categoryDeleteVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "删除分类", method = "DELETE")
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('sys:categories','sys:categories:remove')")
    public ResultVo<Boolean> RemoveCategory(@RequestBody CategoryDeleteVo categoryDeleteVo) throws BusinessException {
        return ResultVo.build(iCategoryService.remove(categoryDeleteVo.getIds()));
    }

    /**
     * 更新分类
     *
     * @param id
     * @param categoryUpdateVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "更新分类", method = "PUT")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('sys:categories','sys:categories:update')")
    public ResultVo<Optional<CategoryListResultDto>> UpdateCategory(@PathVariable(value = "id") @Valid @NotNull @Min(1) Integer id, @RequestBody @Valid CategoryUpdateVo categoryUpdateVo) throws BusinessException {
        return ResultVo.build(iCategoryService.update(id, categoryUpdateVo.getParentId(), categoryUpdateVo.getCategoryName(), categoryUpdateVo.getCategoryDescription(), categoryUpdateVo.getMetaTitle(), categoryUpdateVo.getMetaDescription(), categoryUpdateVo.getFileId(), categoryUpdateVo.getStatus()));
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
    @PreAuthorize("hasAnyAuthority('sys:categories','sys:categories:show')")
    public ResultVo<Optional<CategoryListResultDto>> ShowCategory(@PathVariable(value = "id") @Valid @NonNull @Min(1) Integer id) throws BusinessException {
        return ResultVo.build(iCategoryService.getById(id));
    }

    /**
     * 列表-带分页
     *
     * @param categoryPaginationParameterVo
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "分类列表", method = "POST")
    @PostMapping("/pagination")
    @PreAuthorize("hasAnyAuthority('sys:categories','sys:categories:pagination')")
    public ResultVo<ResultListVo<CategoryListResultDto>> Pagination(@RequestBody @Valid CategoryPaginationParameterVo categoryPaginationParameterVo) throws BusinessException {
        CategoryQueryConditionDto categoryQueryConditionDto = MiscUtils.copyProperties(categoryPaginationParameterVo, CategoryQueryConditionDto.class);
        IPage<CategoryListResultDto> categoryListResultDoIPage = iCategoryService.page(categoryQueryConditionDto, categoryPaginationParameterVo.getPageNumber(), categoryPaginationParameterVo.getPageSize());
        return ResultVo.build(ResultListVo.build(categoryListResultDoIPage.getRecords(), categoryListResultDoIPage.getTotal()));
    }

    /**
     * 列表-不带分页
     *
     * @return
     * @throws BusinessException
     */
    @Operation(summary = "分类列表 - 不带分页", method = "GET")
    @PostMapping("/list")
    @PreAuthorize("hasAnyAuthority('sys:categories','sys:categories:list')")
    public ResultVo<ResultListVo<CategoryListResultDto>> List(@RequestBody(required = false) @Valid CategoryListVo categoryListVo) throws BusinessException {
        if (categoryListVo != null) {
            CategoryQueryConditionDto categoryQueryConditionDto = MiscUtils.copyProperties(categoryListVo, CategoryQueryConditionDto.class);
            List<CategoryListResultDto> list = iCategoryService.list(categoryQueryConditionDto);
            return ResultVo.build(ResultListVo.build(list, list.size()));
        } else {
            List<CategoryListResultDto> list = iCategoryService.list(new CategoryQueryConditionDto());
            return ResultVo.build(ResultListVo.build(list, list.size()));
        }
    }
}
