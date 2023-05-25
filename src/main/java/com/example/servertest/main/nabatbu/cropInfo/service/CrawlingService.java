package com.example.servertest.main.nabatbu.cropInfo.service;

import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.cropInfo.entity.CropOccurInfo;
import com.example.servertest.main.nabatbu.cropInfo.entity.SickList;
import com.example.servertest.main.nabatbu.cropInfo.exception.CropError;
import com.example.servertest.main.nabatbu.cropInfo.exception.CropException;
import com.example.servertest.main.nabatbu.cropInfo.model.response.CropOccurDto;
import com.example.servertest.main.nabatbu.cropInfo.model.response.CropOccurInfoDto;
import com.example.servertest.main.nabatbu.cropInfo.repository.CropOccurInfoRepository;
import com.example.servertest.main.nabatbu.cropInfo.repository.SickListRepository;
import com.example.servertest.main.nabatbu.member.service.MemberService;
import com.example.servertest.main.test.service.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CrawlingService {

    private static final String url = "https://ncpms.rda.go.kr/npms/NewIndcUserListR.np";

    private final MemberService memberService;
    private final TestService testService;

    private final CropOccurInfoRepository cropOccurInfoRepository;
    private final SickListRepository sickListRepository;

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
        Elements selects = document.getElementsByClass("ce").select("td");
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

    public String getUrl2(int idx) {
        return "https://ncpms.rda.go.kr/npms/SicknsInfoListRM.np?" +
                "sSearchWord=&sKncrCode01=&sKncrCode02=&sKncrCode=" +
                "&sch2=&sch3=&sSearchOpt=&pageIndex=" + idx +
                "&sicknsListNo=D00000007";
    }

    public void updateAllData(String url) {
        Connection conn = Jsoup.connect(url);

        Document document = null;
        try {
            document = conn.get();
        } catch (IOException exception) {
            CropException e = new CropException(CropError.FAILED_TO_GET_DATA);
        }

        CropOccurInfoDto dto = getDataList(document);

        CropOccurInfo cropOccurInfo = CropOccurInfo.builder()
                .forecastListSize(dto.getForecastListSize())
                .forecastList(new Gson().toJson(dto.getForecastList()))
                .warningListSize(dto.getWarningListSize())
                .warningList(new Gson().toJson(dto.getWarningList()))
                .watchListSize(dto.getWatchListSize())
                .watchList(new Gson().toJson(dto.getWatchList()))
                .updateDt(LocalDateTime.now())
                .build();

        cropOccurInfoRepository.deleteAll();
        cropOccurInfoRepository.save(cropOccurInfo);
    }

    private CropOccurInfoDto getDataList(Document document) {
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

        return CropOccurInfoDto.builder()
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

    public ServiceResult getNoticeList(String token) throws JsonProcessingException {

        if (memberService.checkToken(token).isFail()) {
            return memberService.checkToken(token);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        CropOccurInfo cropOccurInfo = cropOccurInfoRepository.findTopByOrderByIdDesc();

        List<String> warningList = objectMapper.readValue(cropOccurInfo.getWarningList(), new TypeReference<List>() {
        });
        List<String> watchList = objectMapper.readValue(cropOccurInfo.getWatchList(), new TypeReference<List>() {
        });
        List<String> forecastList = objectMapper.readValue(cropOccurInfo.getForecastList(), new TypeReference<List>() {
        });

        List<CropOccurDto> warningListInput = new ArrayList<>();
        List<CropOccurDto> watchListInput = new ArrayList<>();
        List<CropOccurDto> forecastListInput = new ArrayList<>();

        extractFor(warningList, warningListInput);
        extractFor(watchList, watchListInput);
        extractFor(forecastList, forecastListInput);

        CropOccurInfoDto cropOccurInfoDto = CropOccurInfoDto.builder()
                .warningListSize(cropOccurInfo.getWarningListSize())
                .warningList(warningListInput)
                .watchListSize(cropOccurInfo.getWatchListSize())
                .watchList(watchListInput)
                .forecastListSize(cropOccurInfo.getForecastListSize())
                .forecastList(forecastListInput)
                .build();

        return ServiceResult.success(cropOccurInfoDto);
    }

    private void extractFor(List<String> forecastList, List<CropOccurDto> listInput) {
        for (Object item : forecastList) {
            String key = (String) item;
            String cropName = key.substring(key.indexOf("-") + 1, key.indexOf(")"));
            String sickName = key.substring(key.indexOf(")") + 1, key.length());
            SickList sickList = sickListRepository.findBySickNameKorAndCropName(sickName, cropName);
            if (sickList == null) {
                listInput.add(CropOccurDto.builder().cropName(cropName).sickNameKor(sickName).sickKey(null).build());
            } else {
                listInput.add(CropOccurDto.builder().cropName(cropName).sickNameKor(sickName).sickKey(sickList.getSickKey()).build());
            }
        }
    }

    public void save2(String url) throws IOException, InterruptedException {
        System.out.println(url);
        Connection conn = Jsoup.connect(url);

        Document document = null;
        try {
            document = conn.get();
        } catch (IOException exception) {
            CropException e = new CropException(CropError.FAILED_TO_GET_DATA);
        }

        Elements selects = document.getElementsByClass("tabelRound")
                .select("tbody").select("tr");
        int first;
        StringBuilder sb = new StringBuilder();

        int cnt = 0;
        for (Element item : selects.select("td")) {
            if (cnt % 5 == 2) {
                Thread.sleep(500);
                sb.append(item.toString());
                first = sb.indexOf("(");
                String sickKey = sb.substring(first + 2, first + 11);
                SickList sickList = sickListRepository.findBySickKey(sickKey);
                String cropName = sickList.getCropName();
                String sickNameKor = sickList.getSickNameKor();
                String thumbImg = testService.save2(cropName, sickNameKor);
                sickList.setThumbImg(thumbImg);
                sickListRepository.save(sickList);
                System.out.println("cropName: " + cropName + "/sickNameKor: " + sickNameKor);
                sb.setLength(0);
            }
            cnt++;
        }

    }
}
