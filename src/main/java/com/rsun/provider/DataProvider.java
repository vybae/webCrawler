package com.rsun.provider;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.googlecode.aviator.AviatorEvaluator;
import com.rsun.lib.NowFunction;
import com.rsun.provider.aggregator.Aggregatable;
import com.rsun.provider.aggregator.InnerAggregator;
import com.rsun.service.AuthenticationService;
import com.rsun.util.json.JsonParseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zyong on 2017/1/9.
 */
public abstract class DataProvider {

    @Autowired
    private AuthenticationService authenticationService;
    private InnerAggregator innerAggregator;
    protected Map<String, String> dataSource;
    protected Map<String, String> query;
    private int resultLimit;
    private boolean isUsedForTest = false;
    private long interval = 12 * 60 * 60; // second

    private static final Logger logger = LoggerFactory.getLogger(DataProvider.class);

    static {
        AviatorEvaluator.addFunction(new NowFunction());
    }

    public abstract boolean doAggregationInDataSource();

    public boolean isDataSourceAggInstance() {
        if (this instanceof Aggregatable && doAggregationInDataSource()) {
            return true;
        } else {
            return false;
        }
    }


    public final String[] getColumn(boolean reload) throws Exception {
        String[] columns = null;
        if (isDataSourceAggInstance()) {//如果是聚合数据源，由数据库聚合操作
            columns = ((Aggregatable) this).getColumn();
        } else {//如果是非聚合数据源，抽取所有数据，缓存中应有副本，直接读取。
            checkAndLoad(reload);
            columns = innerAggregator.getColumn();
        }
        Arrays.sort(columns);
        return columns;
    }

    public Map<String, Integer> getColumnType() throws Exception {
        Map<String, Integer> result = null;
        if (isDataSourceAggInstance()) {//如果是聚合数据源，由数据库聚合操作
            result = ((Aggregatable) this).getColumnType();
            return result;
        }
        return result;
    }

    private void checkAndLoad(boolean reload) throws Exception {
        String key = getLockKey();
        synchronized (key.intern()) {
            if (reload || !innerAggregator.checkExist(query)) {//如果页面请求不从缓存取数据，或者缓存中不存在此数据，意味重新写入缓存
                String[][] data = getData();//非聚合状态下，从数据源读取数据并放入缓存
                if (data != null) {
                    innerAggregator.pushData(query, data, interval);
                }
                logger.info("loadData {}", key);
            }
        }
    }

    public String getLockKey() {
        String dataSourceStr = JsonParseUtil.convertString(dataSource);
        String queryStr = JsonParseUtil.convertString(query);
        return Hashing.md5().newHasher().putString(dataSourceStr + queryStr, Charsets.UTF_8).hash().toString();
    }

    abstract public String[][] getData() throws Exception;

    abstract public String getDataByPage(int pageNum, int pageSize) throws Exception;

    public void test() throws Exception {
        getData();
    }

    public void setDataSource(Map<String, String> dataSource) {
        this.dataSource = dataSource;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    public void setResultLimit(int resultLimit) {
        this.resultLimit = resultLimit;
    }

    public int getResultLimit() {
        return resultLimit;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public InnerAggregator getInnerAggregator() {
        return innerAggregator;
    }

    public void setInnerAggregator(InnerAggregator innerAggregator) {
        this.innerAggregator = innerAggregator;
    }

    public boolean isUsedForTest() {
        return isUsedForTest;
    }

    public void setUsedForTest(boolean usedForTest) {
        isUsedForTest = usedForTest;
    }

    /**
     * 测试连接是否成功
     *
     * @return
     * @throws Exception
     */
    public boolean testConnection() throws Exception {
        return false;
    }

    public static void close(Closeable instance) {
        if (Objects.nonNull(instance)) {
            try {
                instance.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取HTTP上下文
     *
     * @param userName 用户名
     * @param password 密码
     * @return HttpClientContext
     */
    public HttpClientContext getHttpContext(String userName, String password) {
        HttpClientContext context = HttpClientContext.create();
        try {
            if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
                CredentialsProvider provider = new BasicCredentialsProvider();
                provider.setCredentials(
                        new AuthScope(AuthScope.ANY),
                        new UsernamePasswordCredentials(userName, password)
                );
                context.setCredentialsProvider(provider);
                AuthCache authCache = new BasicAuthCache();
                context.setAuthCache(authCache);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return context;
        }
    }

    /**
     * 获取数据源字段元信息的JSON字符串
     *
     * @return
     */
    public String getFieldsMetaInfoJSON() {
        return null;
    }

    /**
     * 修复Map.getOrDefault报空指针异常
     *
     * @param key
     * @param defaultValue
     * @return
     */
    protected String getDataSourceValue(String key, String defaultValue) {
        return getMapValue(dataSource, key, defaultValue);
    }

    protected String getDataSourceValue(String key) {
        return getDataSourceValue(key, null);
    }

    protected String getQueryValue(String key, String defaultValue) {
        return getMapValue(query, key, defaultValue);
    }

    protected String getQueryValue(String key) {
        return getQueryValue(key, null);
    }

    /**
     * 修复Map.getOrDefault报空指针异常
     *
     * @param key
     * @param defaultValue
     * @return
     */
    private String getMapValue(Map<String, String> map, String key, String defaultValue) {
        try {
            String value = map.get(key);
            return Objects.isNull(value) ? defaultValue : value;
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            return defaultValue;
        }
    }
}
