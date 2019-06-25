package com.example.demo.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

/**
 * @Description:
 * @author: 西门
 * @Date: 2019/6/5
 * @version: 1.0.0
 */
@RestController
public class TestController {

    @Autowired
    private Jedis jedis;

    @RequestMapping("/mytest")
    public String test1(){
        jedis.set("a","a");
        String s=  jedis.get("a");
        System.out.println(s);
        return s;
    }
}


