package com.rsun.cache;

import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by yfyuan on 2016/11/7.
 */
public class RedisCacheManager<T> implements CacheManagerApi<T> {

    private RedisTemplate<String, T> redisTemplate;

    @Override
    public void put(String key, T data, long expire) {
        redisTemplate.boundValueOps(key).set(data, expire, TimeUnit.MILLISECONDS);
    }

    @Override
    public T get(String key) {
        return redisTemplate.boundValueOps(key).get();
    }

    @Override
    public ArrayList<String> getKey() {
        //连接redis服务器，localhost:6379
        Jedis redis = new Jedis("127.0.0.1", 6379);
        // 获取所有key
        Set<byte[]> keySet = redis.keys("*".getBytes());
        byte[][] keys = keySet.toArray(new byte[keySet.size()][]);
        ArrayList<String> keyList = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            byte[] arr = keys[i];
            for (int j = 0; j < arr.length; j++) {

                //String res = new String(arr[j]);

            }
            //keyList.add(res);
        }
        return keyList;
    }


    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void removeAll() {

    }

    public void setRedisTemplate(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

	 public static String[][] unite(String[][] os1, String[][] os2) {
		  List<String[]> list = new ArrayList<String[]>(Arrays.<String[]>asList(os1));
		  list.addAll(Arrays.<String[]>asList(os2));
		  return list.toArray(os1);
		 }
    
//	@Override
//	public void append(String key, T data, long expire) {
//		String[][] dataFromRedis=(String[][]) get(key);
//		String[][] dataNew=(String[][]) data;
//		  T strs3 = (T) unite(dataFromRedis, dataNew);
//		  redisTemplate.boundValueOps(key).set(strs3, expire, TimeUnit.MILLISECONDS);
////        redisTemplate.boundValueOps(key).append( data.toString() );
////        String[][] dataa = (String[][]) get("5d3e389099b0fa68282555d79250ed4d{\"sql\":\"select * from store_info\\\"}");
//        
////        System.out.println("ccc-----redis--total==========  "+dataa!=null?dataa.length:""  );
//	}
}
