package com.rsun.provider.aggregator.jvm;

import com.rsun.cache.CacheQueue;
import com.rsun.provider.aggregator.InnerAggregator;
import com.rsun.provider.aggregator.struct.AggConfig;
import com.rsun.provider.aggregator.struct.AggregateResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


/**
 * Created by yfyuan on 2017/1/18.
 */
@Service
//@Scope("prototype")
public class JvmAggregator extends InnerAggregator {

    @Override
    public String[][] getData(Object objectKey) {
        String key = getStoredCacheKey(objectKey);
        return rawDataCache.get(key);
    }

    @Override
    public void pushData(Object objectKey, String[][] data, long minutes) {
        String key = getStoredCacheKey(objectKey);
        rawDataCache.put(key, data, minutes * 60 * 1000);
    }

    /*
     * 追加数组
     * */
    @Override
    public void appendData(Object objectKey, String[][] data, long minutes) {
        List<String[][]> list = new ArrayList<>();
        list.add(getData(objectKey));
        list.add(data);
        String[][] newData = list.stream()
                .filter(Objects::nonNull)
                .flatMap(s -> Arrays.stream(s))
                .distinct()
                .collect(Collectors.toList())
                .toArray(new String[0][]);
        pushData(objectKey, newData, minutes);
    }

    public static void main(String[] args) throws InterruptedException {
//        List<String[][]> list = new ArrayList<>();
//        list.add(new String[][]{new String[]{"1", "a"}});
//        list.add(new String[][]{new String[]{"2", "b"}});
//        list.add(new String[][]{new String[]{"2", "b"}});
//        list.stream()
//                .flatMap(s -> Arrays.stream(s))
//                .flatMap(s -> Arrays.stream(s))
//                .distinct().forEach(System.out::println);
//        System.out.println(0x2345 >> 6 > 0x0045);

        ReentrantLock lock = new ReentrantLock();
        Condition cond = lock.newCondition();
        java.util.concurrent.atomic.AtomicInteger i = new AtomicInteger();
        final Thread t1 = new Thread(() -> {
            while(i.get() == 0) {
                System.out.println(11);
            }
        });
        t1.start();

        final  Thread t2 = new Thread(() -> {
            i.getAndIncrement();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(t1.getState());
        });
        t2.start();
    }

    @Override
    public String[] queryDimVals(String columnName, AggConfig config) throws Exception {
        return new String[0];
    }

    @Override
    public String[] getColumn() throws Exception {
        return new String[0];
    }

    @Override
    public Map<String, Integer> getColumnType() throws Exception {
        return null;
    }

    @Override
    public AggregateResult queryAggData(AggConfig ac) throws Exception {
        return null;
    }
}

