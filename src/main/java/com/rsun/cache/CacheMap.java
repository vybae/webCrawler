package com.rsun.cache;

import javax.security.auth.Destroyable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用来存储短暂对象的缓存类，实现Map接口，内部有一个定时器用来清除过期（30秒）的对象。
 * 为避免创建过多线程，没有特殊要求请使用getDefault()方法来获取本类的实例。
 * @author www.zuidaima.com
 * @param <K>
 * @param <V>
 */

public class CacheMap<K, V> extends AbstractMap<K, V> implements AutoCloseable {

    private static final long DEFAULT_TIMEOUT = 30000;
    private static CacheMap<Object, Object> defaultInstance;

    public static synchronized final CacheMap<Object, Object> getDefault() {
        if (defaultInstance == null) {
            defaultInstance = new CacheMap<Object, Object>(DEFAULT_TIMEOUT);
        }
        return defaultInstance;
    }

    private class CacheEntry implements Entry<K, V> {
        long time;
        V value;
        K key;

        CacheEntry(K key, V value) {
            super();
            this.value = value;
            this.key = key;
            this.time = System.currentTimeMillis();
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            return this.value = value;
        }
    }

    private class ClearThread extends Thread {
        ClearThread() {
            setName("clear cache thread");
        }

        public void run() {
            while (true) {
                if(this.interrupted()) {
                    System.out.println("线程已经终止， 循环不再执行");
                    break;
                }
                try {
                    long now = System.currentTimeMillis();
                    Object[] keys = map.keySet().toArray();
                    for (Object key : keys) {
                        CacheEntry entry = map.get(key);
                        if (now - entry.time >= cacheTimeout) {
                            synchronized (map) {
                                map.remove(key);
                            }
                        }
                    }
                    Thread.sleep(cacheTimeout);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private long cacheTimeout;
    private ClearThread clearThread = new ClearThread();
    private Map<K, CacheEntry> map = new HashMap<>();

    public CacheMap(long timeout) {
        this.cacheTimeout = timeout;
        this.clearThread.start();
    }
    public CacheMap() {
        this(DEFAULT_TIMEOUT);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entrySet = new HashSet<>();
        Set<Entry<K, CacheEntry>> wrapEntrySet = map.entrySet();
        for (Entry<K, CacheEntry> entry : wrapEntrySet) {
            entrySet.add(entry.getValue());
        }
        return entrySet;
    }

    @Override
    public V get(Object key) {
        CacheEntry entry = map.get(key);
        return entry == null ? null : entry.value;
    }

    @Override
    public V put(K key, V value) {
        CacheEntry entry = new CacheEntry(key, value);
        synchronized (map) {
            map.put(key, entry);
        }
        return value;
    }

    @Override
    public void close() {
        this.clearThread.interrupt();
    }

}