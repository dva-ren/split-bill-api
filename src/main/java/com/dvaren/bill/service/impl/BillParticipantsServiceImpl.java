package com.dvaren.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.constants.SystemConstants;
import com.dvaren.bill.domain.entity.BillParticipants;
import com.dvaren.bill.domain.entity.Users;
import com.dvaren.bill.mapper.BillsMapper;
import com.dvaren.bill.mapper.UsersMapper;
import com.dvaren.bill.service.BillParticipantsService;
import com.dvaren.bill.mapper.BillParticipantsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private BillsMapper billsMapper;

    @Resource
    private UsersMapper usersMapper;

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
        Map<String, Users> usersMap = new HashMap<>();
        List<BillParticipants> billParticipants = participantsMapper.selectList(new LambdaQueryWrapper<BillParticipants>().eq(BillParticipants::getBillId, userId));
        for (BillParticipants billParticipant : billParticipants) {
            Users user = usersMap.get(billParticipant.getUserId());
            if(user == null){
                user = usersMapper.selectById(billParticipant.getUserId());
                usersMap.put(billParticipant.getUserId(), user);
            }
            billParticipant.setUser(user);
        }
        return billParticipants;
    }

    @Override
    public BillParticipants getBillParticipant(String id) {
        return participantsMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = {ApiException.class})
    public void checkoutBills(List<String> participantIds) throws ApiException {
        for (String billId : participantIds) {
            BillParticipants part = participantsMapper.selectById(billId);
            if(part == null){
                throw new ApiException("该账单不存在,id=" + billId);
            }
            BillParticipants billParticipants = new BillParticipants();
            billParticipants.setId(billId);
            billParticipants.setPaid(SystemConstants.PAID);
            int i = participantsMapper.updateById(billParticipants);
            if(i<0){
                throw new ApiException("结算出错,id="+billId);
            }
            List<BillParticipants> participants = participantsMapper.selectList(new LambdaQueryWrapper<BillParticipants>().eq(BillParticipants::getBillId, part.getBillId()).eq(BillParticipants::getPaid, SystemConstants.UN_PAID));
            if(participants.size() == 0){
                billsMapper.deleteById(part.getBillId());
            }
        }
    }
}




