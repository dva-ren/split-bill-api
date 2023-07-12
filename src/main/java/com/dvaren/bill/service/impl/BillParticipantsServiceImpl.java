package com.dvaren.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.constants.SystemConstants;
import com.dvaren.bill.domain.entity.BillParticipants;
import com.dvaren.bill.service.BillParticipantsService;
import com.dvaren.bill.mapper.BillParticipantsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 025
* @description 针对表【bill_participants】的数据库操作Service实现
* @createDate 2023-07-10 09:15:44
*/
@Service
public class BillParticipantsServiceImpl extends ServiceImpl<BillParticipantsMapper, BillParticipants>
    implements BillParticipantsService{

    @Resource
    private BillParticipantsMapper participantsMapper;

    @Override
    public BillParticipants createBillParticipant(BillParticipants billParticipants) {
        participantsMapper.insert(billParticipants);
        return billParticipants;
    }

    @Override
    public BillParticipants updateBillParticipant(BillParticipants billParticipants) {
        participantsMapper.updateById(billParticipants);
        return billParticipants;
    }

    @Override
    public void removeBillParticipants(String id) throws ApiException {
        BillParticipants billParticipants = participantsMapper.selectById(id);
        if(billParticipants == null){
            throw new ApiException("记录不存在");
        }
        participantsMapper.deleteById(id);
    }

    @Override
    public List<BillParticipants> getsBillParticipantList(String userId) {
        return participantsMapper.selectList(new LambdaQueryWrapper<BillParticipants>().eq(BillParticipants::getUserId, userId));
    }

    @Override
    public BillParticipants getBillParticipant(String id) {
        return participantsMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = {ApiException.class})
    public void checkoutBills(List<String> billIds) throws ApiException {
        for (String billId : billIds) {
            BillParticipants billParticipants = new BillParticipants();
            billParticipants.setId(billId);
            billParticipants.setPaid(SystemConstants.PAID);
            int i = participantsMapper.updateById(billParticipants);
            if(i<0){
                throw new ApiException("结算出错,id="+billId);
            }
        }
    }
}




