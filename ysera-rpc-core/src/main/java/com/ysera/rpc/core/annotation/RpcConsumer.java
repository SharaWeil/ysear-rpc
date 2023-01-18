package com.ysera.rpc.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author admin
 * @ClassName RpcConsumer.java
 * @createTime 2023年01月17日 15:49:00
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcConsumer {

    /**
     * 服务名称
     * @return
     */
    String serviceName();

    /**
     * 提供服务全类名
     * @return
     */
    String clazz();

    /**
     *  版本
     * @return
     */
    int version();

}
