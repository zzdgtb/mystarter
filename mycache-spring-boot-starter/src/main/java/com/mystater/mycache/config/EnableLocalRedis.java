package com.mystater.mycache.config;

import java.lang.annotation.*;

/**
 * @Description:
 * @author: 西门
 * @Date: 2019/6/5
 * @version: 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableLocalRedis {
}
