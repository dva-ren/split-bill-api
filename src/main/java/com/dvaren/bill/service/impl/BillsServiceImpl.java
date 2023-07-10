package com.dvaren.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.domain.entity.Bills;
import com.dvaren.bill.domain.entity.Users;
import com.dvaren.bill.mapper.UsersMapper;
import com.dvaren.bill.service.BillsService;
import com.dvaren.bill.mapper.BillsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 025
* @description 针对表【bills】的数据库操作Service实现
* @createDate 2023-07-10 09:15:44
*/
@Service
public class BillsServiceImpl extends ServiceImpl<BillsMapper, Bills>
    implements BillsService{

    @Resource
    private BillsMapper billsMapper;

    @Resource
    private UsersMapper usersMapper;

    @Override
    public Bills getBill(String billId) {
        Bills bills = billsMapper.selectById(billId);
        bills.setCreator(usersMapper.selectById(bills.getCreatorId()));
        return bills;
    }

    @Override
    public List<Bills> getCreatedBills(String uid) {
        Users user = usersMapper.selectById(uid);
        List<Bills> bills = billsMapper.selectList(new LambdaQueryWrapper<Bills>().eq(Bills::getCreatorId, uid));
        for (Bills bill : bills) {
            bill.setCreator(user);
        }
        return bills;
    }

    @Override
    public Bills createBill(Bills bill) {
        billsMapper.insert(bill);
        return bill;
    }

    @Override
    public Bills updateBill(Bills bill) throws ApiException {
        int i = billsMapper.updateById(bill);
        if(i < 0){
            throw new ApiException("未更新");
        }
        return bill;
    }

    @Override
    public void deleteBill(String id) throws ApiException {
        int i = billsMapper.deleteById(id);
        if(i < 0){
            throw new ApiException("未更新");
        }
    }
}




