/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.data.mysql.dao.podo.theme;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
public class ThemeListConditionPo implements Serializable {

    @Serial
    private static final long serialVersionUID = -467672439017601372L;

    @Schema(description = "Theme ID")
    private Integer id;

    @Schema(description = "Theme Name")
    private String themeName;

    @Schema(description = "Theme Type, 1: open 2: describe 3: custom")
    private Integer themeType;

    @Schema(description = "Soft Delete Flag")
    @TableLogic
    private Integer isDelete;

}
