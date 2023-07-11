package com.dvaren.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.domain.dto.BillInfoDto;
import com.dvaren.bill.domain.entity.*;
import com.dvaren.bill.mapper.ActivitiesMapper;
import com.dvaren.bill.mapper.BillParticipantsMapper;
import com.dvaren.bill.mapper.UsersMapper;
import com.dvaren.bill.service.ActivityParticipantsService;
import com.dvaren.bill.service.BillsService;
import com.dvaren.bill.mapper.BillsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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
    private BillParticipantsMapper participantsMapper;

    @Resource
    private ActivitiesMapper activitiesMapper;

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private ActivityParticipantsService activityParticipantsService;

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
    public List<Bills> getAboutMeBills(String uid) {
        List<Bills> list = new ArrayList<>();
        List<String> billIds = new ArrayList<>();

        List<BillParticipants> billParticipants = participantsMapper
                .selectList(new LambdaQueryWrapper<BillParticipants>().eq(BillParticipants::getUserId, uid).orderByDesc(BillParticipants::getCreateTime));

        for (BillParticipants participant : billParticipants) {
            participant.setUser(usersMapper.selectById(participant.getUserId()));
            billIds.add(participant.getBillId());
        }

        Map<String, Users> usersMap = new HashMap<>();
        for (String billId : billIds) {
            Bills bill = billsMapper.selectById(billId);
            List<BillParticipants> billParticipant = participantsMapper
                    .selectList(new LambdaQueryWrapper<BillParticipants>().eq(BillParticipants::getBillId, billId));
            for (BillParticipants participants : billParticipant) {
                Users user = usersMap.get(participants.getUserId());
                if(user == null){
                    user = usersMapper.selectById(participants.getUserId());
                    usersMap.put(participants.getUserId(), user);
                }
                participants.setUser(user);
            }
            bill.setParticipant(billParticipant);
            list.add(bill);
        }

        return list;
    }

    @Override
    @Transactional(rollbackFor = {ApiException.class})
    public Bills createBill(Bills bill) throws ApiException {
        Activities activities = activitiesMapper.selectById(bill.getActivityId());
        if(activities == null){
            throw new ApiException("活动不存在");
        }
        billsMapper.insert(bill);
        // TODO: 还需要处理参与者信息
        List<String> participantIds = bill.getParticipantIds();
        if(participantIds == null || participantIds.size() == 0){
            throw new ApiException("participant参数错误");
        }
        BigDecimal len = new BigDecimal(bill.getParticipantIds().size());
        BigDecimal splitMoney = bill.getMoney().divide(len,2);
        for (String userId : bill.getParticipantIds()) {
            if(Objects.equals(bill.getCreatorId(), userId)){
                continue;
            }
            Users users = usersMapper.selectById(userId);
            if(users == null){
                throw new ApiException("用户不存在->"+userId);
            }
            BillParticipants billParticipants = new BillParticipants();
            billParticipants.setBillId(bill.getId());
            billParticipants.setUserId(userId);
            // TODO: 分割金额处理   分割不均
            billParticipants.setSplitMoney(splitMoney);
            participantsMapper.insert(billParticipants);
        }
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

    @Override
    public List<Bills> getActivityAllBills(String activityId) {
        List<Bills> bills = billsMapper.selectList(new LambdaQueryWrapper<Bills>().eq(Bills::getActivityId, activityId));
        for (Bills bill : bills) {
            List<BillParticipants> billParticipants = participantsMapper.selectList(new LambdaQueryWrapper<BillParticipants>().eq(BillParticipants::getBillId, bill.getId()));
            for (BillParticipants billParticipant : billParticipants) {
                billParticipant.setUser(usersMapper.selectById(billParticipant.getUserId()));
            }
        }
        return bills;
    }

    /**
     *
     * @param activityId
     * @return
     * [
     *      {
     *          user: Users,
     *          bill: Participant
     *          money: Integer
     *      }
     * ]
     */
    @Override
    public BillInfoDto getTotalMoney(String activityId) {

        // 所有的账单
        List<Bills> allBills = this.getActivityAllBills(activityId);

        // 我创建的账单
        List<Bills> createdBills = this.getCreatedBills(activityId);
        List<BillParticipants> billParticipantList = new ArrayList<>();

        for (Bills createdBill : createdBills) {
            List<BillParticipants> billParticipants = participantsMapper
                    .selectList(new LambdaQueryWrapper<BillParticipants>().eq(BillParticipants::getBillId, createdBill.getId()));
            billParticipantList.containsAll(billParticipants);
        }
        // 所有的参与者
        List<ActivityParticipants> participantList = activityParticipantsService.getParticipant(activityId);


        for (ActivityParticipants participant : participantList) {
            billsMapper.selectById(participant.getUserId());
        }

//        for (Bills bill : allBills) {
//
//        }
//        participantsMapper.selectList(new LambdaQueryWrapper<BillParticipants>().eq());

        return null;
    }
}




