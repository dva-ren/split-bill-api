package com.dvaren.bill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvaren.bill.domain.entity.ActivityParticipants;
import com.dvaren.bill.service.ActivityParticipantsService;
import com.dvaren.bill.mapper.ActivityParticipantsMapper;
import org.springframework.stereotype.Service;

/**
* @author 025
* @description 针对表【activity_participants】的数据库操作Service实现
* @createDate 2023-07-10 09:15:44
*/
@Service
public class ActivityParticipantsServiceImpl extends ServiceImpl<ActivityParticipantsMapper, ActivityParticipants>
    implements ActivityParticipantsService{

}




