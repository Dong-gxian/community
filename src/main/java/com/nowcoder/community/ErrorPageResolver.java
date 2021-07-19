package com.nowcoder.community;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
//自定义ErrorViewResolver来返回自定义的视图
@Component
public class ErrorPageResolver implements ErrorViewResolver {
    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        ModelAndView mv = new ModelAndView();
        switch (status) {
            case NOT_FOUND:
                mv.setViewName("/error/404");
                break;
            default:
                mv.setViewName("/error/5xx");
        }
        return mv;
    }
}

