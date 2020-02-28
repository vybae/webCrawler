package com.rsun.web;

import com.rsun.constant.CrawlTargetUrl;
import com.rsun.dto.HouseInfo;
import com.rsun.dto.QueryCondition;
import com.rsun.dto.StatisticResult;
import com.rsun.dto.http.AnalysisResponseType;
import com.rsun.dto.http.HttpResponseWrapper;
import com.rsun.provider.aggregator.jvm.JvmAggregator;
import com.rsun.service.CrawlService;
import com.rsun.util.html.HtmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/crawl")
public class CrawlController {

    @Value("${cacheExpire}")
    private int cacheExpire;

    @Autowired
    private JvmAggregator jvmAggregator;

    @Autowired
    private CrawlService crawlService;

    @Autowired
    private HtmlUtil htmlUtil;

    @Autowired
    private CrawlTargetUrl crawlTargetUrl;

    @RequestMapping("/getProjectList")
    public String getProjectList(QueryCondition queryCondition, Model model) {
        HttpResponseWrapper<List<String>> rs = crawlService.getProjectList(queryCondition);
        model.addAttribute("result", rs.getResponseContent());
        model.addAttribute("resp", rs);
        model.addAttribute("certListAction", crawlTargetUrl.getFullCertListUrl());
        queryCondition.setPage(String.valueOf(rs.getPage()));
        queryCondition.setTotal(String.valueOf(rs.getTotal()));
        return "crawl/certList";
    }

    @RequestMapping("/getHouseList")
    public String getHouseList(QueryCondition condition, Model model) {
        model.addAttribute("certno", condition.getCertno());
        model.addAttribute("pjname", condition.getPjname());
        model.addAttribute("houseDetailAction", crawlTargetUrl.getFullHouseDetailUrl());
        return "crawl/houseList";
    }

    @ResponseBody
    @RequestMapping("/housesDataJson")
    public HttpResponseWrapper<List<HouseInfo>> housesDataJson(QueryCondition queryCondition, Model model) {
        model.addAttribute("queryCondition", queryCondition);
        HttpResponseWrapper<List<HouseInfo>> result;
        String cacheKey = queryCondition.getIdx();
        if ("true".equals(queryCondition.getRefreshCache()) || !jvmAggregator.checkExist(cacheKey)) {
            result = crawlService.getHouseList(queryCondition);
            if (result.getStatus().equals(AnalysisResponseType.SUCCESS) &&
                    result.getResponseContent().size() > 0) {
                String[][] arr = result.getResponseContent().stream()
                        .map(HouseInfo::toStringArray)
                        .collect(Collectors.toList()).toArray(new String[0][]);
                if (arr.length > 0) {
                    jvmAggregator.pushData(cacheKey, arr, cacheExpire);
                }
            }
        } else {
            String[][] cacheArr = jvmAggregator.getData(cacheKey);
            result = new HttpResponseWrapper<>();
            result.setResponseContent(Arrays.stream(cacheArr != null ? cacheArr : new String[0][]).map(HouseInfo::new).collect(Collectors.toList()));

            String[][] stat = jvmAggregator.getData(HtmlUtil.getDailyCrawlTimesKey());
            result.setCurrentReqCount(stat != null ? Integer.parseInt(stat[0][0]) : 0);
            result.setLimitReqCount(htmlUtil.getDailyCrawlTimesLimit());
            result.setStatus(AnalysisResponseType.SUCCESS);
        }
        return result;
    }

    @RequestMapping("/getHouseStatistic")
    public String getHouseStatistic(QueryCondition queryCondition, Model model) {
        model.addAttribute("queryCondition", queryCondition);
        model.addAttribute("houseDetailAction", crawlTargetUrl.getFullCertDetailUrl());
        model.addAttribute("housePdfAction", crawlTargetUrl.getFullHousePdfString());
        return "crawl/statistic";
    }

    @ResponseBody
    @RequestMapping("/houseStatisticJson")
    public HttpResponseWrapper<List<StatisticResult>> houseStatisticJson(QueryCondition queryCondition, Model model) {
        HttpResponseWrapper<List<HouseInfo>> houses = housesDataJson(queryCondition, model);
        final String certno = queryCondition.getCertno();

        Map<String, String[]> statMap = houses.getResponseContent().stream().collect(Collectors.groupingBy(HouseInfo::getLstusage,
                Collectors.collectingAndThen(Collectors.toList(), list -> {
                    List<HouseInfo> saled = list.stream().filter(h -> "已售".equals(h.getStatus())).collect(Collectors.toList());
                    String idx = list.get(0).getCertidx();
                    long supplyCount = list.size();
                    long saleCount = saled.size();
                    double supplyArea = list.stream().mapToDouble(h -> Double.parseDouble(h.getLstarea())).sum();
                    double saleArea = saled.stream().mapToDouble(h -> Double.parseDouble(h.getLstarea())).sum();
                    return new String[]{idx, String.valueOf(supplyCount), String.valueOf(saleCount), String.valueOf(supplyArea), String.valueOf(saleArea)};
                })));
        HttpResponseWrapper<List<StatisticResult>> resp = houses.copyWithoutContent();
        resp.setResponseContent(StatisticResult.convertMap(certno, statMap));
        return resp;
    }
}
