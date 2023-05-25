package com.example.servertest.main.nabatbu.cropInfo.psis.model.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "service")
@Getter
@Setter
@ToString
public class PsisListService {

    private int totalCount;
    private String buildTime;
    private ListA list;

    @XmlRootElement(name = "list")
    @Getter
    @Setter
    @ToString
    public static class ListA {

        private java.util.List<Item> item;

        @XmlRootElement(name = "item")
        @Getter
        @Setter
        @ToString
        public static class Item {

            private int pestiCode; //농약품목코드 상세정보 조회키
            private int diseaseUseSeq; //병해충사용방법 상세정보 조회키
            private String cropName; //작물명
            private String diseaseWeedName; //적용병해충
            private String useName; //용도
            private String pestiKorName; //품목
            private String pestiBrandName; //상표명
            private String compName; //회사명
            private String engName; //주성분 함량
            private String cmpaItmNm; //제조수입구분
            private String indictSymbl; //작용기작
            private String applyFirstRegDate; //회사등록일
            private String cropCd; //작물코드
            private String cropLrclCd; //작물분류코드
            private String cropLrclNm; //작물분류명
            private String pestiUse; //사용방법
            private String dilutUnit; //희석배수(10a당 사용량)
            private String useSuittime; //안전사용기준(수확~일전)
            private String useNum; //안전사용기준(~회 이내)
            private String wafindex;
        }
    }

    private int displayCount;
    private int startPoint;
}
