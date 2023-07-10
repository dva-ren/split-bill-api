package com.dvaren.bill.service;

import com.dvaren.bill.domain.entity.Activities;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 025
* @description 针对表【activities】的数据库操作Service
* @createDate 2023-07-10 09:15:44
*/
@Service
public interface ActivitiesService extends IService<Activities> {

    Activities createActivity(Activities activities);

    List<Activities> getsTheCreatedActivities(String uid);

    List<Activities> getsJoinActivities(String uid);

    void dissolutionActivity(String id);

}
