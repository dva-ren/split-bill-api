package com.dvaren.bill.service;

import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.domain.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dvaren.bill.domain.vo.UserLoginVo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
* @author 025
* @description 针对表【users】的数据库操作Service
* @createDate 2023-07-10 09:15:44
*/
@Service
public interface UsersService extends IService<Users> {

    Users queryUserById(String uid);

    Users login(HttpServletRequest request) throws ApiException;

    Users register(UserLoginVo userLoginVo) throws ApiException;

    void removeUser(String uid);

}
