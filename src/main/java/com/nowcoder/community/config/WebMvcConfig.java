package com.nowcoder.community.config;

import com.nowcoder.community.controller.interceptor.AlphaInterceptor;
import com.nowcoder.community.controller.interceptor.LoginRequiredInterceptor;
import com.nowcoder.community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AlphaInterceptor interceptor;
    private final LoginTicketInterceptor loginTicketInterceptor;
    private final LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    public WebMvcConfig(AlphaInterceptor interceptor, LoginTicketInterceptor loginTicketInterceptor, LoginRequiredInterceptor loginRequiredInterceptor) {
        this.interceptor = interceptor;
        this.loginTicketInterceptor = loginTicketInterceptor;
        this.loginRequiredInterceptor = loginRequiredInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpg","/*/*.jpeg")
                .addPathPatterns("/register", "/login");

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpg","/*/*.jpeg");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpg","/*/*.jpeg");
    }
}
