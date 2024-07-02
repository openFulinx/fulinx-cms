/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.web.controller.share.article.vo;

import com.fulinx.spring.web.framework.base.BaseParameterVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ShareArticleTypeListVo extends BaseParameterVo {

    @Schema(description = "Article Type")
    private String articleTypeCode;

}
