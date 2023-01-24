package com.rpc.test;

import com.ysera.rpc.core.annotation.EnableYseraRpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author admin
 * @ClassName ConsumerApplication.java
 * @createTime 2023年01月24日 22:09:00
 */
@SpringBootApplication
@EnableYseraRpcClient
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class,args);
    }
}
