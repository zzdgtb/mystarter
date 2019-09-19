package com.mystater.mycache.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author 西门
 * @version 0.1.0
 * @description ${description}
 * @date 2019/9/19
 */
public class TestListiener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("====================================服务器启动完成====================");
    }
}
