package com.dvaren.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvaren.bill.domain.entity.Activities;
import com.dvaren.bill.domain.entity.ActivityParticipants;
import com.dvaren.bill.domain.entity.Users;
import com.dvaren.bill.mapper.ActivityParticipantsMapper;
import com.dvaren.bill.mapper.UsersMapper;
import com.dvaren.bill.service.ActivitiesService;
import com.dvaren.bill.mapper.ActivitiesMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 创建活动
     * @param activities
     * @return
     */
    @Override
    public Activities createActivity(Activities activities) {
        activitiesMapper.insert(activities);
        return activities;
    }

    /**
     * 获取我加入的活动
     * @param uid 用户id
     * @return
     */
    @Override
    public List<Activities> getsJoinActivities(String uid) {
        Map<String, Users> usersMap = new HashMap<>();
        List<ActivityParticipants> activityParticipants = participantsMapper.selectList(new LambdaQueryWrapper<ActivityParticipants>().eq(ActivityParticipants::getUserId, uid));
        List<Activities> activities = new ArrayList<>();
        for (ActivityParticipants participants : activityParticipants) {
            Activities activity = activitiesMapper.selectById(participants.getActivityId());
            String creatorId = activity.getCreatorId();
            Users user = usersMap.get(creatorId);
            if(user == null){
                user = usersMapper.selectById(creatorId);
                usersMap.put(creatorId,user);
            }
            activity.setCreator(user);
            activities.add(activity);
        }
        return activities;
    }

    /**
     * 获取我创建的活动
     * @param uid
     * @return
     */
    @Override
    public List<Activities> getsTheCreatedActivities(String uid) {
        Map<String, Users> usersMap = new HashMap<>();
        List<Activities> users = activitiesMapper.selectList(new LambdaQueryWrapper<Activities>().eq(Activities::getCreatorId, uid));
        for (Activities activities : users) {
            Users user = usersMap.get(activities.getCreatorId());
            if(user == null){
                user = usersMapper.selectById(activities.getCreatorId());
                usersMap.put(activities.getId(),user);
            }
            activities.setCreator(user);
        }
        return users;
    }
    /**
     * 解散活动
     * @param activityId
     */
    @Override
    public void dissolutionActivity(String activityId) {
        participantsMapper.delete(new LambdaQueryWrapper<ActivityParticipants>().eq(ActivityParticipants::getActivityId,activityId));
        activitiesMapper.delete(new LambdaQueryWrapper<Activities>().eq(Activities::getId,activityId));
    }
}




