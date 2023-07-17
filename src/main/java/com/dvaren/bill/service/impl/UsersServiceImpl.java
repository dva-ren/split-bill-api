package com.dvaren.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.constants.SystemConstants;
import com.dvaren.bill.domain.entity.Users;
import com.dvaren.bill.domain.vo.UserLoginVo;
import com.dvaren.bill.service.UsersService;
import com.dvaren.bill.mapper.UsersMapper;
import com.dvaren.bill.utils.JWTUtil;
import com.dvaren.bill.utils.RestUtil;
import com.dvaren.bill.utils.TextUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author 025
* @description 针对表【users】的数据库操作Service实现
* @createDate 2023-07-10 09:15:44
*/
@Service
@Component
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

    @Value("${weixin.appid}")
    public void setAPPID(String APPID) {
        this.APPID = APPID;
    }

    @Value("${weixin.secret}")
    public void setSECRET(String SECRET) {
        this.SECRET = SECRET;
    }

    public static String APPID;

    public static String SECRET;

    @Resource
    private UsersMapper usersMapper;

    @Override
    public Users queryUserById(String uid) throws ApiException {
        Users user = usersMapper.selectById(uid);
        if(user == null){
            throw new ApiException("用户不存在");
        }

        return user;
    }

    @Override
    public Users login(UserLoginVo userVo) throws ApiException {
        return this.register(userVo);
    }

    @Override
    public Users register(UserLoginVo userLoginVo) throws ApiException {
        String openId = new UsersServiceImpl().getOpenId(userLoginVo.getCode());
        if(openId == null){
            throw new ApiException("open_id查询失败");
        }
        Users user = usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getOpenId, openId));
        if(user == null){
            user = new Users();
            user.setAvatar(userLoginVo.getAvatar());
            user.setNickname(userLoginVo.getNickname());
            user.setNickname(userLoginVo.getNickname());
            user.setOpenId(openId);
            usersMapper.insert(user);
        }
        return user;
    }

    @Override
    public void removeUser(String uid) {

    }

    @Override
    public Users updateUser(Users user) throws ApiException {
        if(TextUtil.containEmptyValue(Arrays.asList(user.getNickname(),user.getAvatar(),user.getId()))){
            throw new ApiException("参数错误");
        }
        usersMapper.updateById(user);
        return user;
    }

    private String getOpenId(String code) throws ApiException {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + this.APPID +"&secret="+this.SECRET+"&js_code="+code+"&grant_type=authorization_code";
        HttpHeaders headers = new HttpHeaders();
        try {
            String res = RestUtil.doGetRequest(url, headers);
            Pattern errorReg = Pattern.compile("\"errmsg\":.*?\"(.*?)\"");
            Pattern resReg = Pattern.compile("\"openid\":.*?\"(.*?)\"");
            Matcher errorRes = errorReg.matcher(res);
            Matcher idRes = resReg.matcher(res);
            if(errorRes.find()){
                return null;
            }
            else if (idRes.find()){
                return idRes.group(1);
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            throw new ApiException("API请求异常");
        }
    }
}




