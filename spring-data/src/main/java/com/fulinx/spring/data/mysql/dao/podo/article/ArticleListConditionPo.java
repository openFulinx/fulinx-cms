/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.podo.article;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleListConditionPo implements Serializable {

    @Serial
    private static final long serialVersionUID = -467672439017601372L;

    @Schema(description = "Article Id")
    private Integer articleId;

    @Schema(description = "Article Name")
    private String articleName;

    @Schema(description = "Category ID")
    private Integer categoryId;

    @Schema(description = "status")
    private Boolean status;

    @Schema(description = "Soft Delete Flag")
    @TableLogic
    private Integer isDelete;

}
