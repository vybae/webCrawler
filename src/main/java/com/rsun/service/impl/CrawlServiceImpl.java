package com.rsun.service.impl;

import com.rsun.constant.CrawlTargetUrl;
import com.rsun.dto.HouseInfo;
import com.rsun.dto.QueryCondition;
import com.rsun.dto.Tuple3;
import com.rsun.dto.http.AnalysisResponseType;
import com.rsun.dto.http.HttpResponseWrapper;
import com.rsun.service.CrawlService;
import com.rsun.util.html.HtmlUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CrawlServiceImpl implements CrawlService {

    @Autowired
    private HtmlUtil htmlUtil;

    @Autowired
    private CrawlTargetUrl crawlTargetUrl;

    @Override
    public HttpResponseWrapper<List<String>> getProjectList(QueryCondition condition) {
        HttpResponseWrapper<List<String>> resp = new HttpResponseWrapper<>();

        List<String> projects = new ArrayList<>();
        String url = crawlTargetUrl.getCertListUrl();
        url = condition.attachToUrl(url);

//        JXDocument jxd = JXDocument.create(HtmlUtil.readFile("D:\\Projects\\webCrawler\\src\\main\\webapp\\static\\cacheDownload.html"));
        Tuple3<JXDocument, AnalysisResponseType, Integer> tuple = htmlUtil.getHtmlDocument(url);
        resp.setStatus(tuple.getSecond());
        resp.setCurrentReqCount(tuple.getThird());
        resp.setLimitReqCount(htmlUtil.getDailyCrawlTimesLimit());
        JXDocument jxd = tuple.getFirst();
        if (jxd != null) {
            JXNode pageSel = jxd.selNOne("//select[@id='plpageno']");
            if (pageSel != null) {
                int total = pageSel.asElement().children().size();
                Element cur = pageSel.asElement().selectFirst("option[selected]");
                resp.setPage(cur == null ? 1 : Integer.parseInt(cur.html()));
                resp.setTotal(total);
            }
            String path = "//table[@id='htba_table_data']//table[@id='htba_data_box']";
            List<JXNode> list = jxd.selN(path);
            list.forEach(n -> {
                Element ele = n.asElement();
                String certno = ele.selectFirst("td.certno span").text();
                String html = ele.outerHtml()
                        .replaceFirst("<a href=\"detail\\?idx=([\\-\\w]+)\" target=\"_blank\">([^<]+)</a>",
                                "<input class=\"chxCert\" idx=\"$1\" pjname=\"$2\" " +
                                        "certno=\"" + certno + "\" type=\"checkbox\"/>" +
                                        "<a href=\"/crawl/getHouseList?idx=$1&certno=" + certno +
                                        "&pjname=$2\" target=\"_blank\">$2</a>");
                projects.add(html);
            });
            resp.setResponseContent(projects);
        }
        return resp;
    }

    @Override
    public HttpResponseWrapper<List<HouseInfo>> getHouseDetail(String idx) {
        return null;
    }

    @Override
    public HttpResponseWrapper<List<HouseInfo>> getHouseList(QueryCondition condition) {
        final HttpResponseWrapper<List<HouseInfo>> resp = new HttpResponseWrapper<>();
        List<List<HouseInfo>> list = new ArrayList<>();

        String url = crawlTargetUrl.getHouseListUrl();
        url = condition.attachToUrl(url, true);
        final String certidx = condition.getIdx();
        final String certno = condition.getCertno();
        final String pjname = condition.getPjname();
        final Function<JXDocument, List<HouseInfo>> convertDoc = (jxDocument) -> {
            JXNode data = jxDocument.selNOne("//table[@id='htba_data_table'][2]");
            Elements elements = data.asElement().select("tr");
            List<HouseInfo> result = new ArrayList<>();
            elements.next().forEach(e -> {
                HouseInfo h = new HouseInfo();
                h.setIdx(e.child(7).child(0).attr("href").replaceFirst("detail\\?idx=", ""));
                h.setCertidx(certidx);
                h.setCertno(certno);
                h.setPjname(pjname);
                h.setBuildno(e.child(0).text());
                h.setUnitno(e.child(1).text());
                h.setRoomno(e.child(2).text());
                h.setFloor(e.child(3).text());
                h.setLstusage(e.child(4).text());
                h.setLstarea(e.child(5).text().replaceAll("(?!\\d+\\.\\d{2})\\s+.*", ""));
                h.setStatus(e.child(6).text());
                result.add(h);
            });
            return result;
        };

        Tuple3<JXDocument, AnalysisResponseType, Integer> tuple = htmlUtil.getHtmlDocument(url);
        resp.setStatus(tuple.getSecond());
        resp.setCurrentReqCount(tuple.getThird());
        resp.setLimitReqCount(htmlUtil.getDailyCrawlTimesLimit());
        JXDocument doc = tuple.getFirst();
        if (doc != null) {
            list.add(convertDoc.apply(doc));

            try {
                List<JXNode> pages = doc.selN("//table[@id='table_pagelist']//select[@id='plpageno']/option");
                if (pages.size() > 1) {
                    final CountDownLatch latch = new CountDownLatch(pages.size() - 1);
                    if (pages.size() > 1) {
                        final String pageUrl = url;
                        IntStream.range(2, pages.size() + 1).parallel().forEach(p -> {
                            Tuple3<JXDocument, AnalysisResponseType, Integer> t = htmlUtil.getHtmlDocument(pageUrl + "&page=" + p);
                            resp.setCurrentReqCount(t.getThird());
                            resp.setStatusWhenSuccess(t.getSecond());
                            if (t.getSecond().equals(AnalysisResponseType.SUCCESS)) {
                                JXDocument jd = t.getFirst();
                                list.add(convertDoc.apply(jd));
                            }
                            latch.countDown();
//                            System.out.println("Remains Count: " + latch.getCount());
                        });
                    }
                    latch.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        List<HouseInfo> ss = list.stream().flatMap(s -> s.stream()).collect(Collectors.toList());
        resp.setResponseContent(ss);
        return resp;
    }


}
