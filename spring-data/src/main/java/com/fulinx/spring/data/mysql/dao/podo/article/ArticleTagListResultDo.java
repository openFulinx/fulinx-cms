/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.podo.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleTagListResultDo implements Serializable {
    @Serial
    private static final long serialVersionUID = -2377141739948936231L;

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "Article ID")
    private Integer articleId;

    @Schema(description = "Tag")
    private String tag;

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
}
