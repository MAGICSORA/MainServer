package com.example.servertest.main.nabatbu.cropInfo.ncpms.model.response;

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
public class NcpmsSickService {

    private int totalCount;
    private String buildTime;
    private ListB list;

    @XmlRootElement(name = "list")
    @Getter
    @Setter
    @ToString
    public static class ListB {

        private java.util.List<ItemN> item;

        @XmlRootElement(name = "item")
        @Getter
        @Setter
        @ToString
        public static class ItemN {

            private String oriImg;
            private String thumbImg;
            private String sickKey;
            private String cropName;
            private String sickNameChn;
            private String sickNameKor;
            private String sickNameEng;
        }
    }

    private int displayCount;
    private int startPoint;
}
