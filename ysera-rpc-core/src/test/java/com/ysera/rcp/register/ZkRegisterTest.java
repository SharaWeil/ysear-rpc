package com.ysera.rcp.register;

import com.ysera.rpc.core.registry.RegistryClient;
import com.ysera.rpc.core.registry.RegistryProperties;
import com.ysera.rpc.core.registry.zk.ZookeeperRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author admin
 * @ClassName ZkRegisterTest.java
 * @createTime 2023年01月18日 09:36:00
 */
public class ZkRegisterTest {
    private ZookeeperRegistry serviceRegister;
    private RegistryClient client;

    @Before
    public void before(){
        RegistryProperties registryProperties = new RegistryProperties();
        RegistryProperties.ZookeeperProperties zookeeper = registryProperties.getZookeeper();
        zookeeper.setConnectString("hanzhihuadeMacBook-Pro.local:2181");
        zookeeper.setNamespace("ysear_rpc");
        serviceRegister = new ZookeeperRegistry(registryProperties);
        serviceRegister.start();
        client = new RegistryClient(serviceRegister);
    }


    @Test
    public void testZk(){
        client.register("helloService",new InetSocketAddress(5050));
        client.register("helloService",new InetSocketAddress("192.168.1.101",5050));
        System.out.println(123);
    }

    @After
    public void after(){
        List<InetSocketAddress> helloService = client.children("helloService");
        System.out.println(helloService);
    }
}
