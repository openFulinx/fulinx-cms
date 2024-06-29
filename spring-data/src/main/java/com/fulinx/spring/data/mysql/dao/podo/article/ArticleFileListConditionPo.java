/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.podo.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleFileListConditionPo implements Serializable {

    @Serial
    private static final long serialVersionUID = 4632763046562032102L;

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "文章名称")
    private String articleName;

    @Schema(description = "分类ID")
    private Integer categoryId;

    @Schema(description = "Status, 1:审核自动通过,2:正在审核,3:审核拒绝,4:强制删除")
    private Integer status;

    @Schema(description = "Soft Delete Flag")
    private Integer isDelete;

}
