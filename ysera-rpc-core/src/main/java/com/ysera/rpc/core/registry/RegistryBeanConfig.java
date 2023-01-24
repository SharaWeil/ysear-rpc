package com.ysera.rpc.core.registry;

import com.ysera.rpc.core.registry.zk.ZookeeperRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author admin
 * @ClassName ZkConfig.java
 * @createTime 2023年01月23日 22:54:00
 */
@Configuration
public class RegistryBeanConfig {
    @Autowired
    private RegistryProperties registryProperties;

    @Bean
    @ConditionalOnProperty(prefix = "registry", name = "type", havingValue = "zookeeper")
    public Registry getRegistry(){
        return new ZookeeperRegistry(registryProperties);
    }
}
