package com.rsun.util.html;

import com.rsun.dto.Tuple3;
import com.rsun.dto.http.AnalysisResponseType;
import com.rsun.provider.aggregator.jvm.JvmAggregator;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.seimicrawler.xpath.JXDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

@Component
public class HtmlUtil {

    private static Pattern chinesePat = Pattern.compile("[\u4e00-\u9fa5]");

    public static String parseChineseToHex(String word) {
        StringBuilder s = new StringBuilder();
        if (StringUtils.isNotBlank(word)) {
            word.chars().forEach(c -> {
                try {
                    String sc = String.valueOf((char) c);
                    if (chinesePat.matcher(sc).matches()) {
                        byte[] arr = sc.getBytes("GBK");
                        for (byte b : arr) {
                            s.append("%").append(Integer.toHexString(b & 0x000000ff).toUpperCase());
                        }
                    } else {
                        s.append((char) c);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
        }
        return s.toString();
    }

    @Value("${cacheExpire}")
    private int cacheExpire;
    @Autowired
    private JvmAggregator jvmAggregator;

    @Value("${dailyCrawlTimesLimit}")
    private int dailyCrawlTimesLimit;
    @Value("${crawlTimeoutLimit}")
    private int JSOUP_TIMEOUT;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");

    private final Lock reqLock = new ReentrantLock();

    public Tuple3<JXDocument, AnalysisResponseType, Integer> getHtmlDocument(String url) {
        final String key = getCurrentCrawlTimesKey();
        String[][] v;
        int count = 0;
        reqLock.lock();
        try {
            v = jvmAggregator.getData(key);
            if (v != null && v[0] != null) {
                count = Integer.parseInt(v[0][0]);
                if (count >= dailyCrawlTimesLimit) {
                    return new Tuple3(null, AnalysisResponseType.EXCEED_REQUEST_LIMIT, count);
                }
            } else {
                v = new String[][]{new String[1]};
            }
            count++;
            v[0][0] = String.valueOf(count);
            System.out.println("Consume Times: " + v[0][0] + ", " + new Date());
            jvmAggregator.pushData(key, v, cacheExpire);
        } finally {
            reqLock.unlock();
        }
        try {
            return new Tuple3(JXDocument.create(Jsoup.connect(url).timeout(JSOUP_TIMEOUT).get()), AnalysisResponseType.SUCCESS, count);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Tuple3(null, AnalysisResponseType.HTTP_ERROR, count);
    }

    public String readFile(String filePath) {
        StringBuilder s = new StringBuilder();
        File file = new File(filePath);
        if (file.isFile() && file.exists()) { // 判断文件是否存在
            InputStreamReader read = null;// 考虑到编码格式
            try {
                read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;

                while ((lineTxt = bufferedReader.readLine()) != null) {
                    s.append(lineTxt);
                }
                bufferedReader.close();
                read.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s.toString();
    }

    public static String getCurrentCrawlTimesKey() {
        return "CURRENT_CRAWL_TIMES" + sdf.format(new Date());
    }

    public int getDailyCrawlTimesLimit() {
        return dailyCrawlTimesLimit;
    }
}
