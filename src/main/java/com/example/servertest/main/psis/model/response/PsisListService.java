package com.example.servertest.main.psis.model.response;

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

            private int pestiCode;
            private int diseaseUseSeq;
            private String cropName;
            private String diseaseWeedName;
            private String useName;
            private String pestiKorName;
            private String pestiBrandName;
            private String compName;
            private String engName;
            private String cmpaItmNm;
            private String indictSymbl;
            private String applyFirstRegDate;
            private String cropCd;
            private String cropLrclCd;
            private String cropLrclNm;
            private String pestiUse;
            private String dilutUnit;
            private String useSuittime;
            private String useNum;
            private String wafindex;
        }
    }

    private int displayCount;
    private int startPoint;
}
