package com.dvaren.bill.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName activity_participants
 */
@TableName(value ="activity_participants")
@Data
public class ActivityParticipants implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 活动ID
     */
    private String activityId;

    /**
     * 用户ID
     */
    private String userId;

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