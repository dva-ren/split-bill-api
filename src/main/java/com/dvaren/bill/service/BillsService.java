package com.dvaren.bill.service;

import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.domain.dto.BillInfoDto;
import com.dvaren.bill.domain.entity.Bills;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 025
* @description 针对表【bills】的数据库操作Service
* @createDate 2023-07-10 09:15:44
*/
@Service
public interface BillsService extends IService<Bills> {

    Bills getBill(String billId);

    List<Bills> getCreatedBills(String uid);

    List<Bills> getAboutMeBills(String uid);

    Bills createBill(Bills bill) throws ApiException;

    Bills updateBill(Bills bill) throws ApiException;

    void deleteBill(String id) throws ApiException;

    List<Bills> getActivityAllBills(String activityId);

    BillInfoDto getTotalMoney(String activityId);
}
