package com.fulinx.spring.data.mysql.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * Category Description Table
 * </p>
 *
 * @author fulinx
 * @since 2024-07-05
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_category_detail")
@Schema(name = "TbCategoryDetailEntity", description = "Category Description Table")
public class TbCategoryDetailEntity extends Model<TbCategoryDetailEntity> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Category Detail ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "Category ID")
    private Integer categoryId;

    @Schema(description = "Language ID")
    private Integer languageId;

    @Schema(description = "Category Name")
    private String categoryName;

    @Schema(description = "Category Description")
    private String categoryDescription;

    @Schema(description = "Soft Delete Flag")
    @TableLogic
    private Integer isDelete;

    @Schema(description = "Remark")
    private String remark;

    @Schema(description = "Record Version")
    @Version
    private Integer recordVersion;

    @Schema(description = "Record Create Name")
    @TableField(fill = FieldFill.INSERT)
    private String recordCreateName;

    @Schema(description = "Record Update Name")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String recordUpdateName;

    @Schema(description = "Record Create Time")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime recordCreateTime;

    @Schema(description = "Record Update Time")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime recordUpdateTime;

    public static final String ID = "id";

    public static final String CATEGORY_ID = "category_id";

    public static final String LANGUAGE_ID = "language_id";

    public static final String CATEGORY_NAME = "category_name";

    public static final String CATEGORY_DESCRIPTION = "category_description";

    public static final String IS_DELETE = "is_delete";

    public static final String REMARK = "remark";

    public static final String RECORD_VERSION = "record_version";

    public static final String RECORD_CREATE_NAME = "record_create_name";

    public static final String RECORD_UPDATE_NAME = "record_update_name";

    public static final String RECORD_CREATE_TIME = "record_create_time";

    public static final String RECORD_UPDATE_TIME = "record_update_time";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
