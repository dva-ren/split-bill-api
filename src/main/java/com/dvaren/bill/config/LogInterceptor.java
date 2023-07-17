package com.dvaren.bill.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/***
 * 拦截器：验证用户是否登录
 */
@Configuration
public class LogInterceptor implements HandlerInterceptor {

//    @Resource
//    private ILogService logService;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
//        Log log = new Log();
//        log.setIp(IpUtil.getIpAddr(request));
//        log.setPath(request.getRequestURI());
//        log.setUa(request.getHeader("User-Agent"));
//        logService.addLog(log);
        return true;
    }

}