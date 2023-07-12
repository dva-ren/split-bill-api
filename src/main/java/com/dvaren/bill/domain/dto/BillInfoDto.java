package com.dvaren.bill.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dvaren.bill.domain.entity.Bills;
import com.dvaren.bill.domain.entity.Users;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BillInfoDto {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    private List<Bills> bills;

    private Users user;

    private BigDecimal totalAmount;

}