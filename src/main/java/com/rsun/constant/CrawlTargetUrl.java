package com.rsun.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CrawlTargetUrl {

    @Value("${targetCrawlUrl}")
    private String indexUrl;

    @Value("${certListAction}")
    private String certListUrl;

    @Value("${certDetailAction}")
    private String certDetailUrl;

    @Value("${houseListAction}")
    private String houseListUrl;

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getCertListUrl() {
        return indexUrl + certListUrl;
    }

    public String getCertDetailUrl() {
        return indexUrl + certDetailUrl;
    }

    public String getHouseListUrl() {
        return indexUrl + houseListUrl;
    }
}
