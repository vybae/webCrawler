package com.rsun.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yfyuan on 2017/2/4.
 */
public class EhCacheManager<T> implements CacheManagerApi<T>, InitializingBean, DisposableBean {

    private static CacheManager myCacheManager;

    private Cache<String, CacheObject> cache;

    private String cacheAlias;

    static {
        Configuration xmlConfig = new XmlConfiguration(EhCacheManager.class.getResource("/ehcache.xml"));
        myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        myCacheManager.init();
    }

    @Override
    public ArrayList<String> getKey() {
        return null;
    }

    @Override
    public void put(String key, T data, long expire) {
        cache.put(key, new CacheObject(new Date().getTime(), expire, data));
    }

    @Override
    public T get(String key) {
        CacheObject o = cache.get(key);
        if (o == null) {
            return null;
        } else if (new Date().getTime() >= o.getT1() + o.getExpire()) {
            cache.remove(key);
            return null;
        } else {
            return (T) o.getD();
        }
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
    }

    @Override
    public void removeAll() {

    }

    public void setCacheAlias(String cacheAlias) {
        this.cacheAlias = cacheAlias;
    }

    @Override
    public void afterPropertiesSet() {
        cache = myCacheManager.getCache(cacheAlias, String.class, CacheObject.class);
    }

    @Override
    public void destroy() {
        myCacheManager.close();
    }
}
