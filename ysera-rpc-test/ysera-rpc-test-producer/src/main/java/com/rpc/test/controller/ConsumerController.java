package com.rpc.test.controller;

import com.rpc.test.service.HellWorldConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author admin
 * @ClassName ConsumerController.java
 * @createTime 2023年01月24日 22:11:00
 */
@RequestMapping("/rpc")
@RestController
public class ConsumerController {

    @Autowired
    private HellWorldConsumer hellWorldConsumer;

    @RequestMapping(path = "/hello",method = RequestMethod.GET)
    public String sayHello(String name){
        return hellWorldConsumer.sayHello(name);
    }

    @RequestMapping(path = "/test",method = RequestMethod.GET)
    public String test(String name){
        return  "helloWorld";
    }
}
