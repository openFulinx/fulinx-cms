/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.site.vo;

import com.fulinx.spring.web.framework.base.BaseParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class SiteUpdateVo extends BaseParameterVo {

    @Schema(description = "Domain")
    @NotBlank
    private String domain;

    @Schema(description = "Theme ID")
    @NotNull
    private Integer themeId;

    @Schema(description = "Language ID")
    @NotNull
    private Integer languageId;

    @Schema(description = "Site Name")
    @NotBlank
    private String siteName;

    @Schema(description = "Meta Title")
    @NotBlank
    private String metaTitle;

    @Schema(description = "Meta Description")
    private String metaDescription;

    @Schema(description = "Logo File ID")
    @NotNull
    private Integer logoFileId;

    @Schema(description = "Favicon File ID")
    private Integer faviconFileId;

    @Schema(description = "Status, 0 - Disabled , 1 - Enabled")
    @NotNull
    private Boolean status;
}
