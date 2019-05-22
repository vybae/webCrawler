package com.rsun.dto;

import com.rsun.util.html.HtmlUtil;
import org.apache.commons.lang3.StringUtils;

public class QueryCondition {
    private String idx = "";
    private String certno = "";
    private String pjname = "";
    private String region = "";
    private String key = "";
    private String lstarea = "";
    private String lstusage = "";
    private String page = "";
    private String total = "";
    private String refreshCache = "";
    private String analysisJson = "";

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getCertno() {
        return certno;
    }

    public void setCertno(String certno) {
        this.certno = certno;
    }

    public String getPjname() {
        return pjname;
    }

    public void setPjname(String pjname) {
        this.pjname = pjname;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLstarea() {
        return lstarea;
    }

    public void setLstarea(String lstarea) {
        this.lstarea = lstarea;
    }

    public String getLstusage() {
        return lstusage;
    }

    public void setLstusage(String lstusage) {
        this.lstusage = lstusage;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRefreshCache() {
        return refreshCache;
    }

    public String getAnalysisJson() {
        return analysisJson;
    }

    public void setAnalysisJson(String analysisJson) {
        this.analysisJson = analysisJson;
    }

    public void setRefreshCache(String refreshCache) {
        this.refreshCache = refreshCache;
    }

    public String attachToUrl(String url) {
        return attachToUrl(url, false);
    }

    public String attachToUrl(String url, boolean encode) {
        StringBuilder s = new StringBuilder(url);
        if (!url.endsWith("?")) {
            s.append("?");
        }
        if (StringUtils.isNotBlank(idx)) {
            s.append("&idx=").append(idx);
        }
        if (StringUtils.isNotBlank(certno)) {
            s.append("&certno=").append(certno);
        }
        if (StringUtils.isNotBlank(pjname)) {
            s.append("&pjname=").append(pjname);
        }
        if (StringUtils.isNotBlank(region)) {
            s.append("&region=").append(region);
        }
        if (StringUtils.isNotBlank(key)) {
            s.append("&key=").append(encode ? HtmlUtil.parseChineseToHex(key) : key);
        }
        if (StringUtils.isNotBlank(lstarea)) {
            s.append("&lstarea=").append(lstarea);
        }
        if (StringUtils.isNotBlank(lstusage)) {
            s.append("&lstusage=").append(lstusage);
        }
        if (StringUtils.isNotBlank(page)) {
            s.append("&page=").append(page);
        }
        return s.toString().replaceFirst("\\?&", "?");
    }
}
