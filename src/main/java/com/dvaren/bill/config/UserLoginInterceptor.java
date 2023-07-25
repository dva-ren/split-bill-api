package com.dvaren.bill.config;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dvaren.bill.Annotation.IgnoreAuth;
import com.dvaren.bill.constants.SystemConstants;
import com.dvaren.bill.domain.entity.Users;
import com.dvaren.bill.service.UsersService;
import com.dvaren.bill.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/***
 * 拦截器：验证用户是否登录
 */
@Configuration
public class UserLoginInterceptor implements HandlerInterceptor {

    @Resource
    private UsersService userService;

    private static final Log logger = LogFactory.getLog(SpringApplication.class);

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        //验证是否有忽略验证注解
        IgnoreAuth annotation;
        //验证是否有需要管理员权限注解
//        NeedPermission needPermission;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
//            needPermission = ((HandlerMethod) handler).getMethodAnnotation(NeedPermission.class);
        } else {
            return true;
        }
        // 如果有@IgnoreAuth注解，则不验证token
        if (annotation != null) {
            return true;
        }

        // log.info("验证token")
        Map<String, String> map = new HashMap<>();
        // 获取请求头中的token令牌
        String token = request.getHeader(SystemConstants.ACCESS_TOKEN);
        // log.info("token:" + token)
        try {
            //验证令牌
            JWTUtil.verify(token);
            //token有效，有NeedPermission注解，则需要验证权限
//            if (needPermission != null) {
//                Users userInfo = userService.queryUserById(JWTUtil.getUid(token));
//                if("admin".equals(userInfo.getRoles())){
//                    return true;
//                } else {
//                    map.put("msg", "用户权限不足!");
//                    logger.info("用户权限不足!");
//                }
//            }else{
//                return true;
//            }
            Users user = userService.queryUserById(JWTUtil.getUid(token));
            if(user == null){
                throw new ApiException("用户不存在");
            }
            return true;
        } catch (SignatureVerificationException e) {
            map.put("msg", "无效签名!");
            logger.info("无效签名!");
        } catch (TokenExpiredException e) {
            map.put("msg", "token过期!");
            logger.info("token过期!");
        } catch (AlgorithmMismatchException e) {
            map.put("msg", "token算法不一致!");
            logger.info("token算法不一致!");
        } catch (Exception e) {
            map.put("msg", "token无效!!");
            logger.info("token无效!!");
        } catch (ApiException e) {
            map.put("msg", "用户信息错误");
            logger.info("用户信息错误");
        }

        Res rs = new Res(401, map.get("msg"));
        String json = new ObjectMapper().writeValueAsString(rs);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;

    }

}

class Res{
    Integer code;
    String msg;
    Object data;

    public Res(Integer code, String msg){
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}