package com.ysera.rpc.remote.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @ClassName SpringBeanUtil
 * @createTIme 2023年01月19日 17:28:28
 **/
@Component
public class SpringBeanUtil implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextAware.class);

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringBeanUtil.applicationContext == null) {
            SpringBeanUtil.applicationContext = applicationContext;
        }
        logger.info("\r\n----------加载applicationContext成功-----------------");
    }

    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @param name
     * @return
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 通过class获取Bean.
     * @param <T>
     * @param tClass
     * @return
     */
    public static <T> T getBean(Class<T> tClass){
        return getApplicationContext().getBean(tClass);
    }
}
