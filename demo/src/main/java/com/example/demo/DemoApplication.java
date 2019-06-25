package com.example.demo;

import com.mystater.mycache.config.EnableLocalRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableLocalRedis//开启本地redis，redis starter才会生效
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
