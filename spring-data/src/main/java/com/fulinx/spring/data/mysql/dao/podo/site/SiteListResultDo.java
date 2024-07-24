/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.podo.site;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fulinx.spring.data.mysql.dao.podo.theme.ThemeListResultDo;
import com.fulinx.spring.data.mysql.entity.TbFileEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class SiteListResultDo implements Serializable {

    @Serial
    private static final long serialVersionUID = -7026957750457725266L;

    @Schema(description = "Site ID")
    private Integer id;

    @Schema(description = "Theme ID")
    private Integer themeId;

    @Schema(description = "Theme Vo")
    private ThemeListResultDo themeVo;

    @Schema(description = "Domain")
    private String domain;

    @Schema(description = "Language ID")
    private Integer languageId;

    @Schema(description = "Site Name")
    private String siteName;

    @Schema(description = "Meta Title")
    private String metaTitle;

    @Schema(description = "Meta Description")
    private String metaDescription;

    @Schema(description = "Logo File ID")
    private Integer logoFileId;

    @Schema(description = "Logo File Vo")
    private TbFileEntity logoFileVo;

    @Schema(description = "Favicon File ID")
    private Integer faviconFileId;

    @Schema(description = "Favicon File Vo")
    private TbFileEntity faviconFileVo;

    @Schema(description = "Status, 0 - Disabled , 1 - Enabled")
    private Boolean status;

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
