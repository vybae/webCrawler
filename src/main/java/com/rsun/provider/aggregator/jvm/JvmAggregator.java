package com.rsun.provider.aggregator.jvm;

import com.rsun.provider.aggregator.InnerAggregator;
import com.rsun.provider.aggregator.struct.AggConfig;
import com.rsun.provider.aggregator.struct.AggregateResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by yfyuan on 2017/1/18.
 */
@Service
//@Scope("prototype")
public class JvmAggregator extends InnerAggregator {

    @Override
    public String[][] getData(Object objectKey) {
        String key = getCacheKey(objectKey);
        System.out.println("getData Key:    " + key + ", " + new Date());
        return rawDataCache.get(key);
    }

    @Override
    public void pushData(Object objectKey, String[][] data, long minutes) {
        String key = getCacheKey(objectKey);
        System.out.println("pushData Key:   " + key + ", " + new Date());
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

    public static void main(String[] args) {
        List<String[][]> list = new ArrayList<>();
        list.add(new String[][]{new String[]{"1", "a"}});
        list.add(new String[][]{new String[]{"2", "b"}});
        list.add(new String[][]{new String[]{"2", "b"}});
        list.stream()
                .flatMap(s -> Arrays.stream(s))
                .flatMap(s -> Arrays.stream(s))
                .distinct().forEach(System.out::println);
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

