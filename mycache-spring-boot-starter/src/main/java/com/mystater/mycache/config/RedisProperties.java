package com.mystater.mycache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @author: 西门
 * @Date: 2019/6/5
 * @version: 1.0.0
 */
@ConfigurationProperties(
        prefix = "yundt.cube.center.isales.datasource"
)
@Component
public class RedisProperties {
    /**
     * 指定host
     */
    private String myhost  ;
    /**
     * 指定端口号
     */
    private Integer myport;

    public String getMyhost() {
        return myhost;
    }

    public void setMyhost(String myhost) {
        this.myhost = myhost;
    }

    public Integer getMyport() {
        return myport;
    }

    public void setMyport(Integer myport) {
        this.myport = myport;
    }

    @Override
    public String toString() {
        return "RedisProperties{" +
                "myhost='" + myhost + '\'' +
                ", myport=" + myport +
                '}';
    }
}
