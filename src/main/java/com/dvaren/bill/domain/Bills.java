package com.dvaren.bill.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName bills
 */
@TableName(value ="bills")
@Data
public class Bills implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 账单金额
     */
    private BigDecimal money;

    /**
     * 账单分类
     */
    private String category;

    /**
     * 标题
     */
    private String title;

    /**
     * 创建人ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private String activityId;

    /**
     * 账单描述
     */
    private String description;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 逻辑删除字段(0正常 1删除)
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}