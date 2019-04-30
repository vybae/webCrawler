package com.rsun.service;

import com.rsun.dto.HouseInfo;
import com.rsun.dto.QueryCondition;
import com.rsun.dto.http.HttpResponseWrapper;

import java.util.List;

public interface CrawlService {

    HttpResponseWrapper<List<String>> getProjectList(QueryCondition condition);

    HttpResponseWrapper<List<HouseInfo>> getHouseList(QueryCondition condition);

    HttpResponseWrapper<List<HouseInfo>> getHouseDetail(String idx);
}
