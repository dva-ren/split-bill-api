package com.dvaren.bill.mapper;

import com.dvaren.bill.domain.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 025
* @description 针对表【users】的数据库操作Mapper
* @createDate 2023-07-10 09:15:44
* @Entity com.dvaren.bill.domain.Users
*/
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}




