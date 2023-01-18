package com.ysera.rcp.register;

import com.ysera.rpc.core.registry.RegistryProperties;
import com.ysera.rpc.core.registry.zk.ZkRegistryImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * @author admin
 * @ClassName ZkRegisterTest.java
 * @createTime 2023年01月18日 09:36:00
 */
public class ZkRegisterTest {
    private ZkRegistryImpl serviceRegister;

    @Before
    public void before(){
        serviceRegister = new ZkRegistryImpl(new RegistryProperties());
        serviceRegister.start();
    }


    @Test
    public void testZk(){

    }
}
