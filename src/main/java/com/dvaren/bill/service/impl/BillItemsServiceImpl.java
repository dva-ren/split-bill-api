package com.dvaren.bill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvaren.bill.domain.entity.BillItems;
import com.dvaren.bill.service.BillItemsService;
import com.dvaren.bill.mapper.BillItemsMapper;
import org.springframework.stereotype.Service;

/**
* @author 025
* @description 针对表【bill_items】的数据库操作Service实现
* @createDate 2023-07-10 09:15:44
*/
@Service
public class BillItemsServiceImpl extends ServiceImpl<BillItemsMapper, BillItems>
    implements BillItemsService{

}




