/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.theme.vo;

import com.fulinx.spring.web.framework.base.BasePaginationParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "文件分页查询参数")
public class ThemePaginationParameterVo extends BasePaginationParameterVo {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "File Original Name")
    private String fileOriginalName;

    @Schema(description = "File Type")
    private Integer fileType;

    @Schema(description = "Soft Delete Flag")
    private Integer isDelete;

}
