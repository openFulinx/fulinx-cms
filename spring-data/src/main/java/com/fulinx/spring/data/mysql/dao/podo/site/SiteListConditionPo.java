/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.podo.site;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
public class SiteListConditionPo implements Serializable {

    @Serial
    private static final long serialVersionUID = -467672439017601372L;

    @Schema(description = "Site ID")
    private Integer id;

    @Schema(description = "Domain")
    private String domain;

    @Schema(description = "Language ID")
    private Integer languageId;

    @Schema(description = "Site Name")
    private String siteName;

    @Schema(description = "Soft Delete Flag")
    @TableLogic
    private Integer isDelete;

}
