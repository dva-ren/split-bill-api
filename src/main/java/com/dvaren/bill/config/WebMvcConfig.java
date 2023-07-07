package com.dvaren.bill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author iu
 *  此配置类可配置拦截器、参数解析器、返回值解析器、跨域支持等等
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 处理拦截器Autowired为null问题
     * @return
     */
    @Bean
    public HandlerInterceptor getUserLoginInterceptor(){
        return new LogInterceptor();
    }

    /**
     * 拦截器配置
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getUserLoginInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/swagger-ui");
    }
}