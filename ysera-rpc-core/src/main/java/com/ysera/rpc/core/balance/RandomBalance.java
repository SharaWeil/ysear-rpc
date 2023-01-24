package com.ysera.rpc.core.balance;

import com.ysera.rpc.core.registry.RegistryInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Administrator
 * @ClassName RandomBalance
 * @createTIme 2023年01月19日 17:15:15
 **/

public class RandomBalance implements LoadBalance {
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    @Override
    public RegistryInfo selectOne(List<RegistryInfo> list) {
        int size = list.size();
        int index = RANDOM.nextInt(0, size);
        return list.get(index);
    }
}
