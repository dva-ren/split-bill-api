package com.dvaren.bill.mapper;

import com.dvaren.bill.domain.BillItems;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 025
* @description 针对表【bill_items】的数据库操作Mapper
* @createDate 2023-07-10 09:15:44
* @Entity com.dvaren.bill.domain.BillItems
*/
@Mapper
public interface BillItemsMapper extends BaseMapper<BillItems> {

}



