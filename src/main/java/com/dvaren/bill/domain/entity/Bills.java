package com.dvaren.bill.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.dvaren.bill.constants.BillType;
import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 账单备注
     */
    private String remark;

    /**
     * 账单时间
     */
    @JsonFormat(locale = "zh",timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 创建人ID
     */
    private String creatorId;

    /**
     * 创建人信息
     */
    @TableField(exist = false)
    private Users creator;

    /**
     * 参与者id数组
     */
    @TableField(exist = false)
    private List<String> participantIds;

    /**
     * 参与者信息数组
     */
    @TableField(exist = false)
    private List<BillParticipants> participant;


    /**
     * 活动ID
     */
    private String activityId;

    /**
     * 账单描述
     */
    private String description;

    /**
     * 账单类型(收入或支出)
     */
    @TableField(exist = false)
    private BillType type;

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