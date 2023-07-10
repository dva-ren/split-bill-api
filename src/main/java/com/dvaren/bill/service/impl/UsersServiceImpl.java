package com.dvaren.bill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvaren.bill.domain.Users;
import com.dvaren.bill.service.UsersService;
import com.dvaren.bill.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author 025
* @description 针对表【users】的数据库操作Service实现
* @createDate 2023-07-10 09:15:44
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}



