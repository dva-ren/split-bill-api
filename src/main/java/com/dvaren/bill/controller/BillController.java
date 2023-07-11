package com.dvaren.bill.controller;

import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.constants.SystemConstants;
import com.dvaren.bill.domain.entity.Bills;
import com.dvaren.bill.service.BillsService;
import com.dvaren.bill.utils.JWTUtil;
import com.dvaren.bill.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/bill")
public class BillController {

    @Resource
    private BillsService billsService;

    @GetMapping()
    public ResponseResult<List<Bills>> list(@RequestParam(value = "uid",defaultValue = "") String uid, HttpServletRequest request) throws ApiException {
        if(uid.isEmpty()){
          uid = JWTUtil.getUid(request.getHeader(SystemConstants.ACCESS_TOKEN));
        }
        List<Bills> aboutMeBills = billsService.getAboutMeBills(uid);
        return ResponseResult.ok(aboutMeBills);
    }

    @PostMapping
    public ResponseResult<Object> bill(@RequestBody Bills bills, HttpServletRequest request) throws ApiException {
        String uid = JWTUtil.getUid(request.getHeader(SystemConstants.ACCESS_TOKEN));
        bills.setCreatorId(uid);
        billsService.createBill(bills);
        return ResponseResult.ok("创建成功",null);
    }
}
