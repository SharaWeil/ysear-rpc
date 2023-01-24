package com.ysera.rpc.core.annotation;

import com.ysera.rpc.core.RpcServiceScannerRegister;
import com.ysera.rpc.core.proxy.YseraRpcProxyFactory;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author admin
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
//spring中的注解,加载对应的类
@Import(RpcServiceScannerRegister.class)
@Documented
public @interface EnableYseraRpcClient {
    String[] basePackage() default {};


    Class<? extends YseraRpcProxyFactory> beanFactory() default YseraRpcProxyFactory.class;
}
