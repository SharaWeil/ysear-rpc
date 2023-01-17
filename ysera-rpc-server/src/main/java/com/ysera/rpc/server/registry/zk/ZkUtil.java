package com.ysera.rpc.server.registry.zk;

import com.ysera.rpc.exception.RegistryException;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

/*
 * @Author Administrator
 * @Date 2023/1/18
 **/
public class ZkUtil {
    public static void put(CuratorFramework client, String key, String value, boolean deleteOnDisconnect) {
        final CreateMode mode = deleteOnDisconnect ? CreateMode.EPHEMERAL : CreateMode.PERSISTENT;
        try {
            client.create()
                    .orSetData()
                    .creatingParentsIfNeeded()
                    .withMode(mode)
                    .forPath(key, value.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            throw new RegistryException("Failed to put registry key: " + key, e);
        }
    }

    public static List<String> children(CuratorFramework client,String key) {
        try {
            List<String> result = client.getChildren().forPath(key);
            result.sort(Comparator.reverseOrder());
            return result;
        } catch (Exception e) {
            throw new RegistryException("zookeeper get children error", e);
        }
    }
}
