package com.ysera.rpc.core.registry;

import com.google.common.net.HostAndPort;
import com.ysera.rpc.core.annotation.RpcService;
import com.ysera.rpc.remote.netty.NettyServerProperties;
import com.ysera.rpc.remote.serializer.RpcSerializerType;
import com.ysera.rpc.remote.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author admin
 * @ClassName RegistryClient.java
 * @createTime 2023年01月18日 16:29:00
 */
@Component
public class RegistryClient {

    private static final Logger log = LoggerFactory.getLogger(RegistryClient.class);

    private static final String PATH_SEPARATOR = "/";
    private static final String EMPTY = "";

    private static final Serializer kryo = RpcSerializerType.getSerializerByType((byte) 1);

    private static final CopyOnWriteArrayList<BeanDefinition> NON_REGISTER_SERVICE = new CopyOnWriteArrayList<>();

    @Autowired
    private Registry registry;

    @Autowired
    private NettyServerProperties nettyServerProperties;

    public RegistryClient() {

    }

    public RegistryClient(Registry registry) {
        this.registry = registry;
    }

    @PostConstruct
    void init() throws IOException {
        Iterator<BeanDefinition> iterator = NON_REGISTER_SERVICE.iterator();
        while (iterator.hasNext()) {
            AnnotatedBeanDefinition next = (AnnotatedBeanDefinition) iterator.next();
            AnnotationMetadata metadata = next.getMetadata();
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(RpcService.class.getName());
            assert annotationAttributes != null;
            String serviceName = (String) annotationAttributes.get("serviceName");
            Integer version = (Integer) annotationAttributes.get("version");
            String className = metadata.getClassName();
            RegistryInfo registryInfo = new RegistryInfo(className, serviceName, version);
            register(serviceName, registryInfo, new InetSocketAddress(nettyServerProperties.getAddress(), nettyServerProperties.getPort()));
            NON_REGISTER_SERVICE.remove(next);
        }
    }

    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     * @param address     服务地址
     */
    public void register(String serviceName, RegistryInfo info, InetSocketAddress address) throws IOException {
        serviceName = checkPath(serviceName);
        String path = serviceName + PATH_SEPARATOR + MessageFormat.format("{0}:{1}", address.getHostName(), String.valueOf(address.getPort()));
        byte[] serialize = kryo.serialize(info);
        registry.put(path, serialize, true);
        log.info("register zk success! path:[{}]", path);
    }

    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     * @param address     服务地址
     */
    public void register(String serviceName, InetSocketAddress address) {
        serviceName = checkPath(serviceName);
        String path = serviceName + PATH_SEPARATOR + MessageFormat.format("{0}:{1}", address.getHostName(), String.valueOf(address.getPort()));
        registry.put(path, EMPTY, true);
        log.info("register zk success! path:[{}]", path);
    }


    public List<InetSocketAddress> children(String key) {
        key = checkPath(key);
        return registry.children(key).stream().map(elem -> {
            HostAndPort hostAndPort = HostAndPort.fromString(elem);
            return new InetSocketAddress(hostAndPort.getHost(), hostAndPort.getPort());
        }).collect(Collectors.toList());
    }

    public List<RegistryInfo> childrenInfo(String key) {
        key = checkPath(key);
        final List<RegistryInfo> result = new ArrayList<>();
        try {
            List<String> children = registry.children(key);
            for (String child : children) {
                HostAndPort hostAndPort = HostAndPort.fromString(child);
                child = checkPath(child);
                byte[] info = registry.getInfo(key+child);
                RegistryInfo deserialize = kryo.deserialize(info, RegistryInfo.class);
                deserialize.setAddress(hostAndPort.getHost());
                deserialize.setPort(hostAndPort.getPort());
                result.add(deserialize);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private String checkPath(String path) {
        if (!path.startsWith(PATH_SEPARATOR)) {
            path = PATH_SEPARATOR + path;
        }
        return path;
    }

    public static void addService(BeanDefinition beanDefinition) {
        NON_REGISTER_SERVICE.add(beanDefinition);
    }
}
