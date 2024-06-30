/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.podo.category;

import com.fulinx.spring.data.mysql.entity.TbFileEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class CategoryListResultDo implements Serializable {
    @Serial
    private static final long serialVersionUID = -2377141739948936231L;

    @Schema(description = "Category ID")
    private Integer id;

    @Schema(description = "Parent ID")
    private Integer parentId;

    @Schema(description = "Parent Ids")
    private List<Integer> parentIds;

    @Schema(description = "File ID")
    private Integer fileId;

    @Schema(description = "File Vo")
    private TbFileEntity fileVo;

    @Schema(description = "Language Id")
    private Integer languageId;

    @Schema(description = "Language Code")
    private String languageCode;

    @Schema(description = "Category Name")
    private String categoryName;

    @Schema(description = "Category Description")
    private String categoryDescription;

    @Schema(description = "Meta Title")
    private String metaTitle;

    @Schema(description = "Meta Description")
    private String metaDescription;

    @Schema(description = "Status, 0: Disabled 1: Enabled")
    private Boolean status;

    @Schema(description = "Soft Delete Flag")
    private Integer isDelete;

    @Schema(description = "Remark")
    private String remark;

    @Schema(description = "Record Version")
    private Integer recordVersion;

    @Schema(description = "Record Create Name")
    private String recordCreateName;

    @Schema(description = "Record Update Name")
    private String recordUpdateName;

    @Schema(description = "Record Create Time")
    private LocalDateTime recordCreateTime;

    @Schema(description = "Record Update Time")
    private LocalDateTime recordUpdateTime;

    @Schema(description = "Children")
    private List<CategoryListResultDo> children;
}
