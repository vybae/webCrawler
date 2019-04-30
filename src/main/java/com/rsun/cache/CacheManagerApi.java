package com.rsun.cache;

import java.util.ArrayList;

/**
 * Created by yfyuan on 2016/11/7.
 */
public interface CacheManagerApi<T> {

    void put(String key, T data, long expire);

//    void append(String key, T data, long expire);

    T get(String key);

    default T getOrElse(String key, T defaultValue) {
        T result = get(key);
        return result == null ? defaultValue : result;
    }

    ArrayList<String> getKey();

    void remove(String key);

    void removeAll();
}
