package com.dvaren.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.domain.entity.Activities;
import com.dvaren.bill.domain.entity.ActivityParticipants;
import com.dvaren.bill.mapper.ActivitiesMapper;
import com.dvaren.bill.mapper.UsersMapper;
import com.dvaren.bill.service.ActivityParticipantsService;
import com.dvaren.bill.mapper.ActivityParticipantsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
* @author 025
* @description 针对表【activity_participants】的数据库操作Service实现
* @createDate 2023-07-10 09:15:44
*/
@Service
public class ActivityParticipantsServiceImpl extends ServiceImpl<ActivityParticipantsMapper, ActivityParticipants>
    implements ActivityParticipantsService{

    @Resource
    private ActivityParticipantsMapper activityParticipantsMapper;

    @Resource
    private ActivitiesMapper activitiesMapper;

    @Resource
    private UsersMapper usersMapper;

    /**
     * 向活动添加参与者
     * @param activityId 活动id
     * @param userId 用户id
     * @return 活动参与者信息
     * @throws ApiException Api异常
     */
    @Override
    public ActivityParticipants addParticipant(String activityId, String userId) throws ApiException {
        Activities activity = activitiesMapper.selectById(activityId);
        if(activity == null){
            throw new ApiException("活动不存在");
        }
        if(Objects.equals(activity.getCreatorId(), userId)){
            throw new ApiException("不能加入自己创建的活动");
        }
        ActivityParticipants activityParticipants = new ActivityParticipants();
        activityParticipants.setActivityId(activityId);
        activityParticipants.setUserId(userId);
        activityParticipantsMapper.insert(activityParticipants);
        return activityParticipants;
    }

    /**
     * 移除参与者
     * @param activityId
     * @param userId
     */
    @Override
    public void removeParticipant(String activityId, String userId) {
        LambdaQueryWrapper<ActivityParticipants> eq = new LambdaQueryWrapper<ActivityParticipants>().eq(ActivityParticipants::getActivityId, activityId).eq(ActivityParticipants::getUserId, userId);
        activityParticipantsMapper.delete(eq);
    }

    @Override
    public List<ActivityParticipants> getParticipant(String activityId) {
        List<ActivityParticipants> participantsList = activityParticipantsMapper
                .selectList(new LambdaQueryWrapper<ActivityParticipants>().eq(ActivityParticipants::getActivityId, activityId));

        for (ActivityParticipants participants : participantsList) {
            participants.setUser(usersMapper.selectById(participants.getUserId()));
        }
        return participantsList;
    }
}




