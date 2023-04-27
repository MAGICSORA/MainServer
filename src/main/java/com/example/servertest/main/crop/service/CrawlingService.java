package com.example.servertest.main.crop.service;

import com.example.servertest.main.crop.exception.CropError;
import com.example.servertest.main.crop.exception.CropException;
import com.example.servertest.main.crop.model.response.CrawlingDto;
import com.example.servertest.main.global.model.ServiceResult;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CrawlingService {

    private static final String url = "https://ncpms.rda.go.kr/npms/NewIndcUserListR.np";

    public String getIndex() {
        Connection conn = Jsoup.connect(url);

        Document document = null;
        try {
            document = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str = getIdxData(document);
        return str;
    }

    private String getIdxData(Document document) {
        Elements selects = document.getElementsByClass("ce").select("td");    //⭐⭐⭐
        return selects.get(0).text();
    }

    public String getUrl(String idx) {
        int newIdx = Integer.parseInt(idx) + 1;
        return "https://ncpms.rda.go.kr/npms/NewIndcUserR.np" +
                "?indcMon=&indcSeq=" + newIdx +
                "&ncpms.cmm.token.html.TOKEN=fa4e9829685ce57111e0693fa934e9a4" +
                "&pageIndex=1&sRegistDatetm=&eRegistDatetm=" +
                "&sCrtpsnNm=&sIndcSj=";
    }

    public ServiceResult getAllData(String url) {
        Connection conn = Jsoup.connect(url);

        Document document;
        try {
            document = conn.get();
        } catch (IOException exception) {
            CropException e = new CropException(CropError.FAILED_TO_GET_DATA);
            return ServiceResult.fail(String.valueOf(e.getCropError()), e.getMessage());
        }

        CrawlingDto str = getDataList(document);
        return ServiceResult.success(str);
    }

    private CrawlingDto getDataList(Document document) {
        Elements warningSelects = document.getElementsByClass("warning");
        Elements watchSelects = document.getElementsByClass("watch");
        Elements forecastSelects = document.getElementsByClass("forecast");

        List warningList = new ArrayList();
        List watchList = new ArrayList();
        List forecastList = new ArrayList();

        StringBuilder sb = new StringBuilder();

        extractData(warningSelects, warningList, sb);
        extractData(watchSelects, watchList, sb);
        extractData(forecastSelects, forecastList, sb);

        return CrawlingDto.builder()
                .warningList(warningList)
                .watchList(watchList)
                .forecastList(forecastList)
                .warningListSize(warningList.size())
                .watchListSize(watchList.size())
                .forecastListSize(forecastList.size()).build();
    }

    private void extractData(Elements warningSelects, List warningList, StringBuilder sb) {
        int first;
        int last;
        for (Element e : warningSelects.select("a")) {
            sb.append(e.toString());
            first = sb.indexOf(">");
            last = sb.lastIndexOf("<");
            warningList.add(sb.substring(first + 1, last));
            sb.setLength(0);
        }
    }
}
