package com.example.servertest.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "service")
@Getter
@Setter
public class SickService {

    @XmlElement(name = "totalCount")
    private int totalCount;

    @XmlElement(name = "buildTime")
    private String buildTime;

    @XmlElement(name = "list")
    private List list;

    @Getter
    @Setter
    @XmlRootElement(name = "list")
    private static class List {

        private java.util.List<Item> item;

        @Getter
        @Setter
        @XmlRootElement(name = "item")
        private static class Item {

            private String oriImg;
            private String thumbImg;
            private String sickKey;
            private String cropName;
            private String sickNameChn;
            private String sickNameKor;
            private String sickNameEng;
        }
    }

    @XmlElement(name = "displayCount")
    private int displayCount;

    @XmlElement(name = "startPoint")
    private int startPoint;
}
