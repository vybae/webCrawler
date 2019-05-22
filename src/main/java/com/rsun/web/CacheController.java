package com.rsun.web;

import com.rsun.dto.ProjectInfo;
import com.rsun.provider.aggregator.jvm.JvmAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("cache")
public class CacheController {

    @Value("${cacheExpire}")
    private int cacheExpire;

    @Autowired
    private JvmAggregator jvmAggregator;

    private static String CACHE_CERT_KEY = "CACHE_CERT_KEY";

    @ResponseBody
    @RequestMapping("selectedCertList")
    public List<ProjectInfo> selectedCertList() {
        String[][] arr = jvmAggregator.getData(CACHE_CERT_KEY);
        List<ProjectInfo> list = new ArrayList<>();
        if (arr != null) {
            list.addAll(Arrays.stream(arr).map(s -> {
                ProjectInfo p = new ProjectInfo(s);
                p.setCached(jvmAggregator.checkExist(p.getIdx()));
                return p;
            }).collect(Collectors.toList()));
        }
        return list;
    }

    @ResponseBody
    @RequestMapping("addSelectedCert")
    public String addSelectedCert(@RequestBody List<ProjectInfo> projects) {
        String[][] tmpa = jvmAggregator.getData(CACHE_CERT_KEY);
        List<ProjectInfo> tmp = Arrays.stream(tmpa == null ? new String[0][] : tmpa).map(ProjectInfo::new).collect(Collectors.toList());
        List<String[]> list = Stream.concat(tmp.stream(), projects.stream())
                .distinct()
                .map(ProjectInfo::toStringArray)
                .collect(Collectors.toList());
        jvmAggregator.pushData(CACHE_CERT_KEY, list.toArray(new String[0][]), cacheExpire);
        return "{\"certCount\": " + list.size() + ",\"success\": true}";
    }

    @ResponseBody
    @RequestMapping("removeCert")
    public String removeCert(@RequestParam("idx") String idx) {
        String[][] tmpa = jvmAggregator.getData(CACHE_CERT_KEY);
        List<String[]> list = Arrays.stream(tmpa == null ? new String[0][] : tmpa)
                .map(ProjectInfo::new)
                .filter(p -> !p.getIdx().equals(idx))
                .map(ProjectInfo::toStringArray)
                .collect(Collectors.toList());
        jvmAggregator.pushData(CACHE_CERT_KEY, list.toArray(new String[0][]), cacheExpire);
        return "{\"certCount\": " + list.size() + ",\"success\": true}";
    }

    @ResponseBody
    @RequestMapping("removeAllCerts")
    public String removeAllCerts() {
        jvmAggregator.cleanExist(CACHE_CERT_KEY);
        return "{\"certCount\": 0,\"success\": true}";
    }

    @ResponseBody
    @RequestMapping("testData")
    public List<ProjectInfo> testData() {
        String[][] arr = new String[][]{new String[]{"1", "1", "1"}, new String[]{"2", "1", "1"}, new String[]{"3", "1", "1"}};
        List<ProjectInfo> list = Arrays.stream(arr).map(ProjectInfo::new).collect(Collectors.toList());
        return list;
    }
}
