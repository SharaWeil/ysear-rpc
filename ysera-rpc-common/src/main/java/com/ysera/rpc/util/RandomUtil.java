package com.ysera.rpc.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/*
 * @author Administrator
 * @ClassName RandomUtil
 * @createTIme 2023年01月19日 11:02:02
 **/
public class RandomUtil {
    private static final Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    public static long randomLong(){
        return snowflake.nextId();
    }
}
