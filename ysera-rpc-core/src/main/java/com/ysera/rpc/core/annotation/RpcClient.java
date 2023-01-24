package com.ysera.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * @author admin
 *
 * Comment on the rpc server side
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcClient {

    /**
     * 服务名称
     */
    String value();

    /**
     *
     * 服务ID
     * @return
     */
    int version();

    boolean primary() default true;
}
