package com.dvaren.bill.controller;

import com.dvaren.bill.Annotation.IgnoreAuth;
import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.constants.SystemConstants;
import com.dvaren.bill.domain.entity.Users;
import com.dvaren.bill.domain.vo.UserLoginVo;
import com.dvaren.bill.service.UsersService;
import com.dvaren.bill.utils.JWTUtil;
import com.dvaren.bill.utils.ResponseResult;
import com.dvaren.bill.utils.TextUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UsersController {

    @Resource
    private UsersService usersService;

    @Validated
    @IgnoreAuth
    @PostMapping("/login")
    public ResponseResult<Object> login(@RequestBody UserLoginVo userVo, HttpServletRequest request) throws ApiException {
        String token = request.getHeader(SystemConstants.ACCESS_TOKEN);
        Users user;
        if(TextUtil.isEmpty(token)){
            user = usersService.register(userVo);
        }
        else{
            user = usersService.login(request);
        }
        // 生成token
        Map<String, String> payload = new HashMap<>();
        payload.put("id",user.getId());
        HashMap<String, Object> res = new HashMap<>();
        res.put("user", user);
        res.put("access_token",JWTUtil.generateToken(payload));
        return ResponseResult.ok(res);
    }
}
