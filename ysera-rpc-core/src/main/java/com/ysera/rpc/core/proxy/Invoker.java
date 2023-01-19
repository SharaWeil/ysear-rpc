package com.ysera.rpc.core.proxy;

import com.ysera.rpc.remote.Request;

/*
 * @author Administrator
 * @ClassName RequestHandler
 * @createTIme 2023年01月19日 10:34:34
 **/
public interface Invoker {

    Object invoke(String serviceName, Request request);
}
