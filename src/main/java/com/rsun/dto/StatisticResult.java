package com.rsun.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticResult {
    private String idx;
    private String certno;
    private String lstusage;
    private long supplyCount;
    private long saledCount;
    private double supplyArea;
    private double saledArea;

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

    public String getLstusage() {
        return lstusage;
    }

    public void setLstusage(String lstusage) {
        this.lstusage = lstusage;
    }

    public long getSupplyCount() {
        return supplyCount;
    }

    public void setSupplyCount(long supplyCount) {
        this.supplyCount = supplyCount;
    }

    public long getSaledCount() {
        return saledCount;
    }

    public void setSaledCount(long saledCount) {
        this.saledCount = saledCount;
    }

    public double getSupplyArea() {
        return supplyArea;
    }

    public void setSupplyArea(double supplyArea) {
        this.supplyArea = supplyArea;
    }

    public double getSaledArea() {
        return saledArea;
    }

    public void setSaledArea(double saledArea) {
        this.saledArea = saledArea;
    }

    public StatisticResult() {
    }

    public StatisticResult(String certno, String lstusage, String idx, String supplyCount, String saledCount, String supplyArea, String saledArea) {
        this.certno = certno;
        this.lstusage = lstusage;
        this.idx = idx;
        this.supplyCount = Long.parseLong(supplyCount);
        this.saledCount = Long.parseLong(saledCount);
        this.supplyArea = Double.parseDouble(supplyArea);
        this.saledArea = Double.parseDouble(saledArea);
    }

    public static List<StatisticResult> convertMap(String certno, Map<String, String[]> map) {
        List<StatisticResult> list = new ArrayList<>();
        map.forEach((k,v) -> {
            list.add(new StatisticResult(certno, k, v[0], v[1], v[2], v[3], v[4]));
        });
        return list;
    }
}
