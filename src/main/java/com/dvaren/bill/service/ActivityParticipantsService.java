package com.dvaren.bill.service;

import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.domain.entity.ActivityParticipants;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 025
* @description 针对表【activity_participants】的数据库操作Service
* @createDate 2023-07-10 09:15:44
*/
@Service
public interface ActivityParticipantsService extends IService<ActivityParticipants> {

    ActivityParticipants addParticipant(String activityId, String userId) throws ApiException;

    void removeParticipant(String activityId, String userId);

    List<ActivityParticipants> getParticipant(String activityId);
}
