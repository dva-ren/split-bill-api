package com.dvaren.bill.mapper;

import com.dvaren.bill.domain.entity.Bills;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 025
* @description 针对表【bills】的数据库操作Mapper
* @createDate 2023-07-10 09:15:44
* @Entity com.dvaren.bill.domain.entity.Bills
*/
@Mapper
public interface BillsMapper extends BaseMapper<Bills> {
    @Select("SELECT * FROM bills WHERE activity_id = #{activityId} ORDER BY create_time DESC")
    List<Bills> selectAllBills(String activityId);
}




