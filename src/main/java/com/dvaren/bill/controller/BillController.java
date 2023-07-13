package com.dvaren.bill.controller;

import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.constants.SystemConstants;
import com.dvaren.bill.domain.dto.BillInfoDto;
import com.dvaren.bill.domain.entity.Bills;
import com.dvaren.bill.service.BillParticipantsService;
import com.dvaren.bill.service.BillsService;
import com.dvaren.bill.utils.JWTUtil;
import com.dvaren.bill.utils.ResponseResult;
import com.dvaren.bill.utils.TextUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/bill")
public class BillController {

    @Resource
    private BillsService billsService;

    @Resource
    private BillParticipantsService participantsService;

    @GetMapping()
    public ResponseResult<List<Bills>> list(
            @RequestParam(value = "uid",defaultValue = "") String uid,
            @RequestParam(value = "activityId",defaultValue = "") String activityId,
            HttpServletRequest request) throws ApiException {
        if(uid.isEmpty()){
          uid = JWTUtil.getUid(request.getHeader(SystemConstants.ACCESS_TOKEN));
        }
        List<Bills> aboutMeBills = billsService.getAboutMeBills(uid, activityId, SystemConstants.UN_PAID);
        return ResponseResult.ok(aboutMeBills);
    }

    @PostMapping
    public ResponseResult<Object> createBill(@RequestBody Bills bills, HttpServletRequest request) throws ApiException {
        String uid = JWTUtil.getUid(request.getHeader(SystemConstants.ACCESS_TOKEN));
        bills.setCreatorId(uid);
        billsService.createBill(bills);
        return ResponseResult.ok("创建成功",null);
    }

    @GetMapping("/total")
    public ResponseResult<Object> billTotal(
                @RequestParam(value = "activityId",defaultValue = "") String activityId,
            @RequestParam(value = "type",defaultValue = "") String type,
            HttpServletRequest request) throws ApiException {
        Object res = null;
        String uid = JWTUtil.getUid(request.getHeader(SystemConstants.ACCESS_TOKEN));
        if(TextUtil.isEmpty(type)){
            Map<String, List<BillInfoDto>> data = new HashMap<>();
            data.put("income", billsService.getIncomeTotalMoney(uid,activityId, SystemConstants.UN_PAID));
            data.put("expend", billsService.getExpendTotalMoney(uid,activityId, SystemConstants.UN_PAID));
            res = data;
        }
        else if("income".equals(type)){
            res = billsService.getIncomeTotalMoney(uid, activityId, SystemConstants.UN_PAID);
        } else if ("expend".equals(type)) {
            res = billsService.getExpendTotalMoney(uid, activityId, SystemConstants.UN_PAID);
        }
        return ResponseResult.ok(res);
    }

    @PostMapping("/checkout")
    public ResponseResult<Object> bill(@RequestParam("ids")List<String> ids, HttpServletRequest request) throws ApiException {
        // String uid = JWTUtil.getUid(request.getHeader(SystemConstants.ACCESS_TOKEN));
        participantsService.checkoutBills(ids);
        return ResponseResult.ok("结算成功",null);
    }
    @GetMapping("/query")
    public ResponseResult<Object> queryBills(@RequestParam("ids")List<String> ids) throws ApiException {
        List<Bills> bills = billsService.queryBills(ids);
        return ResponseResult.ok(bills);
    }
}
