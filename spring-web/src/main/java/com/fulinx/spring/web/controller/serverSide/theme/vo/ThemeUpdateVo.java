/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.serverSide.theme.vo;

import com.fulinx.spring.web.framework.base.BaseParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class ThemeUpdateVo extends BaseParameterVo {

    @Schema(description = "Theme Name")
    @NotBlank
    private String themeName;

    @Schema(description = "Theme Type")
    @NotNull
    private Integer themeType;

    @Schema(description = "Theme Author")
    @NotBlank
    private String themeAuthor;

    @Schema(description = "Theme Version")
    @NotBlank
    private String themeVersion;

    @Schema(description = "Theme Thumb File ID")
    private Integer themeThumbFileId;
}
