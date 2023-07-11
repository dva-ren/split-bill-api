package com.dvaren.bill.service;

import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.domain.entity.BillParticipants;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 025
* @description 针对表【bill_participants】的数据库操作Service
* @createDate 2023-07-10 09:15:44
*/
@Service
public interface BillParticipantsService extends IService<BillParticipants> {

    BillParticipants createBillParticipant(BillParticipants billParticipants);

    BillParticipants updateBillParticipant(BillParticipants billParticipants);

    void removeBillParticipants(String id) throws ApiException;

    List<BillParticipants> getsBillParticipantList(String userId);

    BillParticipants getBillParticipant(String id);
}
