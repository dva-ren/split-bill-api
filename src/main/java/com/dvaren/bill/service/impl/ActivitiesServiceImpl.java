package com.dvaren.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.domain.entity.Activities;
import com.dvaren.bill.domain.entity.ActivityParticipants;
import com.dvaren.bill.domain.entity.Users;
import com.dvaren.bill.mapper.ActivityParticipantsMapper;
import com.dvaren.bill.mapper.UsersMapper;
import com.dvaren.bill.service.ActivitiesService;
import com.dvaren.bill.mapper.ActivitiesMapper;
import com.dvaren.bill.service.BillsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
* @author 025
* @description 针对表【activities】的数据库操作Service实现
* @createDate 2023-07-10 09:15:44
*/
@Service
public class ActivitiesServiceImpl extends ServiceImpl<ActivitiesMapper, Activities>
    implements ActivitiesService{

    @Resource
    private ActivitiesMapper activitiesMapper;

    @Resource
    private ActivityParticipantsMapper participantsMapper;

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private BillsService billsService;

    /**
     * 创建活动
     * @param activities 活动实体
     * @return 添加后的结果
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Activities createActivity(Activities activities) {
        activitiesMapper.insert(activities);
        ActivityParticipants activityParticipants = new ActivityParticipants();
        activityParticipants.setActivityId(activities.getId());
        activityParticipants.setUserId(activities.getCreatorId());
        participantsMapper.insert(activityParticipants);
        return activities;
    }

    /**
     * 获取我加入的活动
     * @param uid 用户id
     * @return 活动列表
     */
    @Override
    public List<Activities> getsJoinActivities(String uid) {
        List<ActivityParticipants> activityParticipants = participantsMapper.selectList(new LambdaQueryWrapper<ActivityParticipants>().eq(ActivityParticipants::getUserId, uid));
        List<Activities> activities = new ArrayList<>();
        for (ActivityParticipants participants : activityParticipants) {
            Activities activity = activitiesMapper.selectById(participants.getActivityId());
            activities.add(activity);
        }
        this.setCreator(activities);
        return activities;
    }

    /**
     * 获取我创建的活动
     * @param uid 创建者id
     * @return 活动列表
     */
    @Override
    public List<Activities> getsTheCreatedActivities(String uid) {
        List<Activities> users = activitiesMapper.selectList(new LambdaQueryWrapper<Activities>().eq(Activities::getCreatorId, uid));
        this.setCreator(users);
        return users;
    }
    /**
     * 解散活动
     * @param activityId 活动id
     */
    @Override
    public void dissolutionActivity(String activityId, String uid) throws ApiException {
        System.out.println(activityId);
        Activities activities = activitiesMapper.selectById(activityId);
        if(activities == null){
            throw new ApiException("活动不存在");
        }
        if(!Objects.equals(activities.getCreatorId(), uid)){
            throw new ApiException("权限不足");
        }
        // TODO: 解散前需要判断账单是否都结算完
        if(!billsService.allBillIChecked(activityId)){
            throw new ApiException("还有账单未结算");
        }
        participantsMapper.delete(new LambdaQueryWrapper<ActivityParticipants>().eq(ActivityParticipants::getActivityId, activityId));
        activitiesMapper.deleteById(activityId);
    }

    @Override
    public List<Activities> getsActivities(String uid) {

        return null;
    }

    @Override
    public void exitActivity(String activityId, String uid) throws ApiException {
        int delete = participantsMapper.delete(new LambdaQueryWrapper<ActivityParticipants>()
                .eq(ActivityParticipants::getActivityId, activityId)
                .eq(ActivityParticipants::getUserId, uid));
        if(delete < 0){
            throw new ApiException("操作失败");
        }
    }

    /**
     * 查询创建者谢谢
     * @param activities 原始数据
     * @return 添加后的数据
     */
    public List<Activities> setCreator(List<Activities> activities){
        Map<String, Users> usersMap = new HashMap<>();
        for (Activities activity : activities) {
            Users user = usersMap.get(activity.getCreatorId());
            if(user == null){
                user = usersMapper.selectById(activity.getCreatorId());
                usersMap.put(activity.getCreatorId(),user);
            }
            List<ActivityParticipants> participantsList = participantsMapper.selectList(new LambdaQueryWrapper<ActivityParticipants>().eq(ActivityParticipants::getActivityId, activity.getId()));
            for (ActivityParticipants participants : participantsList) {
                Users partUser = usersMap.get(participants.getUserId());
                if(partUser == null){
                    partUser = usersMapper.selectById(participants.getUserId());
                    usersMap.put(participants.getUserId(),partUser);
                }
                participants.setUser(partUser);
            }
            activity.setParticipants(participantsList);
            activity.setCreator(user);
        }
        return activities;
    }
}




