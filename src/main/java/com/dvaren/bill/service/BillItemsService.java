package com.dvaren.bill.service;

import com.dvaren.bill.domain.entity.BillItems;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 025
* @description 针对表【bill_items】的数据库操作Service
* @createDate 2023-07-10 09:15:44
*/
@Service
public interface BillItemsService extends IService<BillItems> {

    BillItems getBill(String billId);

    List<BillItems> getCreatedBills(String billId);

    BillItems createBill(BillItems billItems);

    BillItems updateBill(BillItems billItems);

    void deleteBill(String id);

}
