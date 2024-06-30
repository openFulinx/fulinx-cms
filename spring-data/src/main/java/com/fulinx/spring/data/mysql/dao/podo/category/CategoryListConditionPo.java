/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.podo.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class CategoryListConditionPo implements Serializable {

    @Serial
    private static final long serialVersionUID = 4632763046562032102L;

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "Status, 0: Disabled 1: Enabled")
    private Boolean status;

    @Schema(description = "Soft Delete Flag")
    private Integer isDelete;

}
