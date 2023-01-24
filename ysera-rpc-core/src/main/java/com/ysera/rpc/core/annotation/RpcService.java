package com.ysera.rpc.core.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author admin
 * @ClassName RpcConsumer.java
 * @createTime 2023年01月17日 15:49:00
 *
 * If the interface or class standard post is annotated, then this class is a service
 * provider that will be registered to zk
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcService {

    /**
     * Service name, please remain unique
     * @return
     */
    String serviceName() default "";

    /**
     *  service version
     * @return
     */
    int version();

    boolean primary() default true;

}
