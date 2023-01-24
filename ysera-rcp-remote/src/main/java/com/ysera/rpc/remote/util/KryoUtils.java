package com.ysera.rpc.remote.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoCallback;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.*;
import java.util.*;

/**
 * @author admin
 * @ClassName KryoUtils.java
 * @createTime 2023年01月24日 19:33:00
 */
public class KryoUtils {
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    });

    public static <T> byte[] serialize(T obj) throws IOException {
        ByteArrayOutputStream os = null;
        Output output = null;
        if (null != obj) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            try {
                os = new ByteArrayOutputStream();
                output = new Output(os);
                kryo.writeObject(output, obj);
                close(output);
                return os.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(os);

            }
        }
        return null;
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        ByteArrayInputStream is = null;
        Input input = null;
        if (null != bytes && bytes.length > 0 && null != clz) {
            try {
                Kryo kryo = KRYO_THREAD_LOCAL.get();
                is = new ByteArrayInputStream(bytes);
                input = new Input(is);
                return kryo.readObject(input, clz);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(is);
                close(input);
            }
        }
        return null;
    }


    /**
     * 使用ThreadLocal创建Kryo
     * 把java对象转序列化存储在文件中;
     *
     * @param obj java对象
     * @return
     */
    public static <T> boolean serializeFile(T obj, String path) {
        if (null != obj) {
            Output output = null;
            try {
                Kryo kryo = KRYO_THREAD_LOCAL.get();
                output = new Output(new FileOutputStream(path));
                kryo.writeObject(output, obj);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(output);
            }
        }
        return false;
    }

    /**
     * 使用ThreadLocal创建Kryo
     * 把序列化的文件反序列化成指定的java对象
     *
     * @param path 文件路径
     * @param t    指定的java对象
     * @param <T>
     * @return 指定的java对象
     */
    public static <T> T unSerializeFile(String path, Class<T> t) {
        if (null != path && null != t) {
            Input input = null;
            try {
                Kryo kryo = KRYO_THREAD_LOCAL.get();
                input = new Input(new FileInputStream(path));
                return kryo.readObject(input, t);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(input);
            }
        }
        return null;
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
