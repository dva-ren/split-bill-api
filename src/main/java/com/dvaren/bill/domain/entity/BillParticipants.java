package com.dvaren.bill.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName bill_participants
 */
@TableName(value ="bill_participants")
@Data
public class BillParticipants implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 账单ID
     */
    private String billId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户信息
     */
    @TableField(exist = false)
    private Users user;

    /**
     * 分摊金额
     */
    private BigDecimal splitMoney;

    /**
     * 是否固定金额
     */
    private Integer fixed;

    /**
     * 是否已支付
     */
    private Integer paid;

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