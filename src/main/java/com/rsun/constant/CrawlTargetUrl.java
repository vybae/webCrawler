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

    @Value("${houseDetailAction}")
    private String houseDetailUrl;

    @Value("${housePdfAction}")
    private String housePdfString;

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getFullCertListUrl() {
        return indexUrl + certListUrl;
    }

    public String getFullCertDetailUrl() {
        return indexUrl + certDetailUrl;
    }

    public String getFullHouseDetailUrl() {
        return indexUrl + houseDetailUrl;
    }

    public String getFullHousePdfString() {
        return indexUrl + housePdfString;
    }

    public String getFullHouseListUrl() {
        return indexUrl + houseListUrl;
    }

    public String getCertListUrl() {
        return certListUrl;
    }

    public String getCertDetailUrl() {
        return certDetailUrl;
    }

    public String getHouseListUrl() {
        return houseListUrl;
    }

    public String getHouseDetailUrl() {
        return houseDetailUrl;
    }

    public String getHousePdfString() {
        return housePdfString;
    }
}
