package com.dvaren.bill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvaren.bill.domain.entity.Bills;
import com.dvaren.bill.service.BillsService;
import com.dvaren.bill.mapper.BillsMapper;
import org.springframework.stereotype.Service;

/**
* @author 025
* @description 针对表【bills】的数据库操作Service实现
* @createDate 2023-07-10 09:15:44
*/
@Service
public class BillsServiceImpl extends ServiceImpl<BillsMapper, Bills>
    implements BillsService{

}




