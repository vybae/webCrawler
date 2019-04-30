package com.rsun.provider.aggregator;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.rsun.cache.CacheManagerApi;
import com.rsun.util.json.JsonParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;

/**
 * Created by zyong on 2017/1/9.
 */
public abstract class InnerAggregator implements Aggregatable {

    @Autowired
    @Qualifier("rawDataCache")
    protected CacheManagerApi<String[][]> rawDataCache;

    public InnerAggregator() {
    }

    public abstract void pushData(Object objectKey, String[][] data, long expire);

    public abstract void appendData(Object objectKey, String[][] data, long expire);

    public abstract String[][] getData(Object objectKey);

    //获取KEY值
    protected String getCacheKey(Object object) {
//        System.out.println("-------------"+JSONObject.toJSON(dataSource).toString() + JSONObject.toJSON(query).toString()+"-----------");
        return Hashing.md5().newHasher().putString(
                JsonParseUtil.convertString(object),
                Charsets.UTF_8).hash().toString();
    }

    public boolean checkExist(Object objectKey) {
        String key = getCacheKey(objectKey);
        System.out.println("checkExist Key: " + key + ", " + new Date());
        return rawDataCache.get(key) != null;
    }

    public void cleanExist(Object objectKey) {
        rawDataCache.remove(getCacheKey(objectKey));
    }

    public void cleanAll() {
        rawDataCache.removeAll();
    }
}
