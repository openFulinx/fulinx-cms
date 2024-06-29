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
public class ArticleCategoryListConditionPo implements Serializable {

    @Serial
    private static final long serialVersionUID = 4632763046562032102L;

    @Schema(description = "Article Category ID")
    private Integer id;

    @Schema(description = "Article ID")
    private Integer articleId;

    @Schema(description = "Category ID")
    private Integer categoryId;

    @Schema(description = "Soft Delete Flag")
    private Integer isDelete;

}
