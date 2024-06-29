/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.file.vo;

import com.fulinx.spring.web.framework.base.BaseParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class FileCreateVo extends BaseParameterVo {

    @Schema(description = "File Type，1：image，2：video 3：pdf 4: excel 5: other", required = true)
    @NotNull
    private Integer fileType;

    @Schema(description = "File Original Name", required = true)
    @NotBlank
    private String fileOriginalName;

    @Schema(description = "File Original Path")
    private String fileOriginalPath;

    @Schema(description = "File Extension Name", required = true)
    @NotBlank
    private String fileExtensionName;

    @Schema(description = "File Key", required = true)
    @NotBlank
    private String fileKey;

    @Schema(description = "File Content Type", required = true)
    @NotBlank
    private String fileContentType;

    @Schema(description = "etag", required = true)
    @NotBlank
    private String etag;

    @Schema(description = "File Size")
    private Long fileSize;
}
