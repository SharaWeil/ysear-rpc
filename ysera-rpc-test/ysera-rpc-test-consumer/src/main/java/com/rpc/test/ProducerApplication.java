package com.rpc.test;

import com.ysera.rpc.core.annotation.EnableYseraRpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author admin
 * @ClassName ProducerApplication.java
 * @createTime 2023年01月24日 22:05:00
 */
@SpringBootApplication
@EnableYseraRpcClient
public class ProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class,args);
    }
}
