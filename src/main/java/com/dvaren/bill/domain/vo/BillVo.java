package com.dvaren.bill.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BillVo {

    private static final long serialVersionUID = 1L;

    private String category;

    private String remark;

    private String activityId;

    private String description;

    private BigDecimal money;

    private List<String> participants;
}
