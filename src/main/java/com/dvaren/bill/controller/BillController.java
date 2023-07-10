package com.dvaren.bill.controller;

import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.constants.SystemConstants;
import com.dvaren.bill.domain.entity.Bills;
import com.dvaren.bill.domain.vo.BillVo;
import com.dvaren.bill.service.BillsService;
import com.dvaren.bill.utils.JWTUtil;
import com.dvaren.bill.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/bill")
public class BillController {

    @Resource
    private BillsService billsService;

    @GetMapping("")
    public ResponseResult list(){
        return ResponseResult.ok();
    }

    @PostMapping
    public ResponseResult<Object> bill(@RequestBody BillVo billVo, HttpServletRequest request) throws ApiException {
        Bills bill = new Bills();
        String uid = JWTUtil.getUid(request.getHeader(SystemConstants.ACCESS_TOKEN));
        bill.setCreatorId(uid);
        bill.setDescription(billVo.getDescription());
        bill.setCategory(billVo.getCategory());
        bill.setActivityId(billVo.getActivityId());
        bill.setMoney(billVo.getMoney());
        bill.setRemark(billVo.getRemark());
        billsService.createBill(bill);
        // TODO: 还需要处理参与者信息
        return ResponseResult.ok();
    }
}
