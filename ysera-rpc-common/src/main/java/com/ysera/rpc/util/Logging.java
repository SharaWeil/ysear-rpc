package com.ysera.rpc.util;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * @author admin
 * @ClassName Logging.java
 * @createTime 2023年01月17日 16:05:00
 */
public class Logging {
    private Logger logger = null;

    private static boolean initializeLogging = false;

    private ClassLoader classLoader = null;

    public Logging(){
        try {
            classLoader = this.getClass().getClassLoader();
            Class<?> bridgeClass = Class.forName("org.slf4j.bridge.SLF4JBridgeHandler", true, classLoader);
            bridgeClass.getMethod("removeHandlersForRootLogger").invoke(null);
            Boolean isInstalled = (Boolean) bridgeClass.getMethod("isInstalled").invoke(null);
            if (!isInstalled) {
                bridgeClass.getMethod("install").invoke(null);
            }
            initializeLogIfNecessary(false);
            logger = LoggerFactory.getLogger(logName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String logName(){
        return this.getClass().getName();
    }

    private void initializeLogIfNecessary(boolean isInterpreter) {
        if (!initializeLogging){
            synchronized (Logging.class){
                initializeLogging(isInterpreter);
            }
        }
    }

    private void initializeLogging(boolean isInterpreter) {
        String binderClass = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();
        String defaultLogClassName = "org.slf4j.impl.Log4jLoggerFactory";
        if (defaultLogClassName.equals(binderClass)){
            if (!LogManager.getRootLogger().getAllAppenders().hasMoreElements()) {
                String defaultProperties = "log4j.properties";
                PropertyConfigurator.configure(this.getClass().getResourceAsStream(defaultProperties));
            }

            if (isInterpreter){
                org.apache.log4j.Logger rootLogger = LogManager.getRootLogger();
                org.apache.log4j.Logger replLogger = LogManager.getLogger(logName());
                Level level = Optional.ofNullable(replLogger.getLevel()).orElse(Level.WARN);
                if (level != rootLogger.getEffectiveLevel()){
                    rootLogger.setLevel(level);
                }
            }
            initializeLogging = true;
        }
    }
}
