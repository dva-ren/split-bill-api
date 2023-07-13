package com.dvaren.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.constants.SystemConstants;
import com.dvaren.bill.domain.dto.BillInfoDto;
import com.dvaren.bill.domain.entity.*;
import com.dvaren.bill.mapper.*;
import com.dvaren.bill.service.ActivityParticipantsService;
import com.dvaren.bill.service.BillParticipantsService;
import com.dvaren.bill.service.BillsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private ActivityParticipantsMapper activityParticipantsMapper;

    @Resource
    private BillParticipantsService billParticipantsService;


    @Resource
    private UsersMapper usersMapper;

    /**
     * 获取账单信息
     * @param billId 账单id
     * @return 账单信息
     */
    @Override
    public Bills getBill(String billId) {
        Bills bills = billsMapper.selectById(billId);
        bills.setCreator(usersMapper.selectById(bills.getCreatorId()));
        bills.setParticipant(billParticipantsService.getsBillParticipantList(billId));
        return bills;
    }

    /**
     * 获取我创建的账单
     * @param uid 用户id
     * @return 账单列表
     */
    @Override
    public List<Bills> getCreatedBills(String uid, String activityId,Integer state) {
        Users user = usersMapper.selectById(uid);
        List<Bills> bills = billsMapper
                .selectList(new LambdaQueryWrapper<Bills>()
                        .eq(Bills::getCreatorId, uid)
                        .eq(Bills::getActivityId,activityId)
                        .orderByDesc(Bills::getCreateTime));
        for (Bills bill : bills) {
            bill.setCreator(user);
            bill.setParticipant(participantsMapper
                    .selectList(new LambdaQueryWrapper<BillParticipants>()
                            .eq(BillParticipants::getBillId, bill.getId())
                            .eq(BillParticipants::getPaid,state)));
        }
        return bills;
    }

    /**
     * 获取和我有关的账单
     * @param uid 用户id
     * @return 账单列表
     */
    @Override
    public List<Bills> getAboutMeBills(String uid, String activityId, Integer state) {
        // TODO: 逻辑有问题
        List<Bills> list = new ArrayList<>();
        List<String> billIds = new ArrayList<>();

        List<BillParticipants> billParticipants = participantsMapper
                .selectList(new LambdaQueryWrapper<BillParticipants>()
                        .eq(BillParticipants::getUserId, uid)
                        .eq(BillParticipants::getPaid, state)
                        .orderByDesc(BillParticipants::getCreateTime));

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

    /**
     * 创建账单
     * @param bill 账单实体
     * @return 添加后的结果
     * @throws ApiException 参数错误
     */
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
        BigDecimal splitMoney = bill.getMoney().divide(len, RoundingMode.CEILING);
        for (String userId : bill.getParticipantIds()) {
            if(Objects.equals(bill.getCreatorId(), userId)){
                continue;
            }
            Users users = usersMapper.selectById(userId);
            if(users == null){
                throw new ApiException("用户不存在->"+userId);
            }
            ActivityParticipants participants = activityParticipantsMapper.selectOne(new LambdaQueryWrapper<ActivityParticipants>().eq(ActivityParticipants::getActivityId, bill.getActivityId()));
            if(participants == null){
                throw new ApiException("该成员不在此活动中,id=" + userId);
            }
            BillParticipants billParticipants = new BillParticipants();
            billParticipants.setBillId(bill.getId());
            billParticipants.setUserId(userId);
            // TODO: 分割金额处理   分割可能不均
            billParticipants.setSplitMoney(splitMoney);
            billParticipants.setPayToUserId(bill.getCreatorId());
            participantsMapper.insert(billParticipants);
        }
        return bill;
    }

    /**
     * 更新账单
     * @param bill 账单实体
     * @return 更新后的账单实体
     * @throws ApiException api异常
     */
    @Override
    public Bills updateBill(Bills bill) throws ApiException {
        int i = billsMapper.updateById(bill);
        if(i < 0){
            throw new ApiException("未更新");
        }
        return bill;
    }

    /**
     * 删除账单
     * @param id 账单id
     * @throws ApiException api异常
     */
    @Override
    public void deleteBill(String id) throws ApiException {
        int i = billsMapper.deleteById(id);
        if(i < 0){
            throw new ApiException("未更新");
        }
    }

    /**
     * 获取指定活动的所有账单
     * @param activityId 活动id
     * @return 账单列表
     */
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
     * 统计支付给我的
     * @param activityId 活动id
     * @return 结果数组
     */
    @Override
    public List<BillInfoDto> getIncomeTotalMoney(String uid, String activityId, Integer state) {
        List<Bills> createdBills = this.getCreatedBills(uid, activityId,state);
        return this.toBillInfoDto(createdBills,state);
    }

    /**
     * 统计支付给别人的
     * @param uid 自己的id
     * @param activityId 活动id
     * @return 结果数组
     */
    @Override
    public List<BillInfoDto> getExpendTotalMoney(String uid, String activityId, Integer state) {
        List<Bills> aboutMeBills = this.getAboutMeBills(uid, activityId,state);
        return this.toBillInfoDto(aboutMeBills,state);
    }

    @Override
    public Boolean allBillIChecked(String activityId) {
        List<Bills> bills = billsMapper.selectList(new LambdaQueryWrapper<Bills>().eq(Bills::getActivityId, activityId));
        for (Bills bill : bills) {
            List<BillParticipants> billParticipants = participantsMapper
                    .selectList(new LambdaQueryWrapper<BillParticipants>()
                            .eq(BillParticipants::getBillId, bill.getId())
                            .eq(BillParticipants::getPaid, SystemConstants.UN_PAID));
            if(billParticipants.size() != 0){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Bills> queryBills(List<String> billIds) {
        List<Bills> bills = new ArrayList<>();
        for (String billId : billIds) {
            bills.add(this.getBill(billId));
        }
        return bills;
    }

    /**
     * 转换为BIllInfoDto
     * @param billsList 账单列表
     * @return 结果数组
     */
    public List<BillInfoDto> toBillInfoDto(List<Bills> billsList, Integer state){
        Map<Users, BigDecimal> decimalMap = new HashMap<>();
        Map<String, Users> usersMap = new HashMap<>();
        Map<String, List<Bills>> userBillMap = new HashMap<>();

        for (Bills bill : billsList) {
            List<BillParticipants> billParticipants = participantsMapper.selectList(new LambdaQueryWrapper<BillParticipants>()
                    .eq(BillParticipants::getBillId, bill.getId())
                    .eq(BillParticipants::getPaid, state));
            for (BillParticipants billParticipant : billParticipants) {
                Users user = usersMap.get(billParticipant.getUserId());
                if(user == null){
                    user = usersMapper.selectById(billParticipant.getUserId());
                    usersMap.put(billParticipant.getUserId(), user);
                }
                BigDecimal bigDecimal = decimalMap.get(user);
                if(bigDecimal == null){
                    bigDecimal = billParticipant.getSplitMoney();
                    decimalMap.put(user, bigDecimal);
                }
                else{
                    bigDecimal = bigDecimal.add(billParticipant.getSplitMoney());
                }
                decimalMap.put(user,bigDecimal);
                List<Bills> bills = userBillMap.computeIfAbsent(billParticipant.getUserId(), k -> new ArrayList<>());
                bills.add(bill);
            }
        }

        List<BillInfoDto> res = new ArrayList<>();

        for (Users user : decimalMap.keySet()) {
            BillInfoDto billInfoDto = new BillInfoDto();
            billInfoDto.setBills(userBillMap.get(user.getId()));
            billInfoDto.setUser(user);
            billInfoDto.setTotalAmount(decimalMap.get(user));
            res.add(billInfoDto);
        }
        return res;
    }
}




