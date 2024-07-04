/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.podo.article;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fulinx.spring.data.mysql.entity.TbArticleTagEntity;
import com.fulinx.spring.data.mysql.entity.TbFileEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleListResultDo implements Serializable {

    @Serial
    private static final long serialVersionUID = -7026957750457725266L;

    @Schema(description = "Article ID")
    private Integer id;

    @Schema(description = "Article Type")
    private Integer articleType;

    @Schema(description = "Article Type Label")
    private String articleTypeLabel;

    @Schema(description = "Category Id")
    private Integer categoryId;

    @Schema(description = "Language ID")
    private Integer languageId;

    @Schema(description = "Article Name")
    private String articleName;

    @Schema(description = "Description")
    private String articleDescription;

    @Schema(description = "Customs")
    private String customs;

    @Schema(description = "Meta Title")
    private String metaTitle;

    @Schema(description = "Meta Description")
    private String metaDescription;

    @Schema(description = "Category Ids")
    private List<List<Integer>> categoryIds;

    @Schema(description = "Status, 0 - Disabled , 1 - Enabled")
    private Boolean status;

    @Schema(description = "File List")
    private List<TbFileEntity> fileList;

    @Schema(description = "Tags")
    private List<TbArticleTagEntity> tags;

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
