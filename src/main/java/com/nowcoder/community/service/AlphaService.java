package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
//@Scope("prototype")
public class AlphaService {
    private final AlphaDao alphaDao;

    @Autowired
    AlphaService(AlphaDao alphaDao) {
        this.alphaDao = alphaDao;
        System.out.println("实例化alphaService");
        System.out.println("alphaService的DAO组件：" + alphaDao);
    }

    @PostConstruct
    public void init() {
        System.out.println(" 初始化alphaService");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("销毁alphaService");
    }

    public String find() {
        return alphaDao.select();
    }
}
