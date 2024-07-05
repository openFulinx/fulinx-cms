/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.podo.theme;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fulinx.spring.data.mysql.entity.TbFileEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class ThemeListResultDo implements Serializable {

    @Serial
    private static final long serialVersionUID = -7026957750457725266L;

    @Schema(description = "Theme ID")
    private Integer id;

    @Schema(description = "Theme Name")
    private String themeName;

    @Schema(description = "Theme Type, 1: open 2: describe 3: custom")
    private Integer themeType;

    @Schema(description = "Theme Author")
    private String themeAuthor;

    @Schema(description = "Theme Version")
    private String themeVersion;

    @Schema(description = "Theme Thumb File ID")
    private Integer themeThumbFileId;

    @Schema(description = "Theme Thumb File Vo")
    private TbFileEntity themeThumbFileVo;

    @Schema(description = "Theme Config")
    private String themeConfig;

    @Schema(description = "Soft Delete Flag")
    @TableLogic
    private Integer isDelete;

    @Schema(description = "Remark")
    private String remark;

    @Schema(description = "Record Version")
    @Version
    private Integer recordVersion;

    @Schema(description = "Record Create Name")
    private String recordCreateName;

    @Schema(description = "Record Update Name")
    private String recordUpdateName;

    @Schema(description = "Record Create Time")
    private LocalDateTime recordCreateTime;

    @Schema(description = "Record Update Time")
    private LocalDateTime recordUpdateTime;
}
