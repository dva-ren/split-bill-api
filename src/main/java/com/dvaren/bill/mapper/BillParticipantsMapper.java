package com.dvaren.bill.mapper;

import com.dvaren.bill.domain.entity.BillParticipants;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 025
* @description 针对表【bill_participants】的数据库操作Mapper
* @createDate 2023-07-10 09:15:44
* @Entity com.dvaren.bill.domain.entity.BillParticipants
*/
@Mapper
public interface BillParticipantsMapper extends BaseMapper<BillParticipants> {

    @Select("SELECT * FROM bill_participants WHERE bill_id = #{billId}")
    List<BillParticipants> selectAllParticipants(String billId);
}




