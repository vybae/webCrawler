package com.rsun.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by yfyuan on 2016/11/7.
 */
public class HeapCacheManager<T> implements CacheManagerApi<T> {

    private ConcurrentMap<String, CacheObject> cache = new ConcurrentHashMap<>();

    @Override
    public void put(String key, T data, long expire) {
        cache.put(key, new CacheObject(new Date().getTime(), expire, data));
    }

    @Override
    public T get(String key) {
        CacheObject o = cache.get(key);
        if (o == null || new Date().getTime() >= o.getT1() + o.getExpire())
            return null;
        else {
            return (T) o.getD();
        }
    }

    @Override
    public ArrayList<String> getKey() {
        return new ArrayList<>(cache.keySet());
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
    }

    @Override
    public void removeAll() {

    }

    public boolean containsKey(String key) {return cache.containsKey(key);}

}
