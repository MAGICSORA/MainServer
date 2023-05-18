package com.example.servertest.main.api.ncpms.model.response;

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
public class NcpmsSickDetailService {

    private VirusImgList virusImgList;

    @XmlRootElement(name = "virusImgList")
    @Getter
    @Setter
    @ToString
    public static class VirusImgList {

        private java.util.List<Item1> item;

        @XmlRootElement(name = "item")
        @Getter
        @Setter
        @ToString
        public static class Item1 {

            private String imageTitle;
            private String image;
        }
    }

    private String etc;
    private String preventionMethod;
    private String symptoms;

    private ImageList imageList;

    @XmlRootElement(name = "imageList")
    @Getter
    @Setter
    @ToString
    public static class ImageList {

        private java.util.List<Item2> item;

        @XmlRootElement(name = "item")
        @Getter
        @Setter
        @ToString
        public static class Item2 {

            private String imageTitle;
            private String iemSpchcknNm;
            private String image;
            private String iemSpchcknCode;
        }
    }

    private VirusList virusList;

    @XmlRootElement(name = "virusList")
    @Getter
    @Setter
    @ToString
    public static class VirusList {

        private java.util.List<Item3> item;

        @XmlRootElement(name = "item")
        @Getter
        @Setter
        @ToString
        public static class Item3 {

            private String sfeNm;
            private String virusName;
        }
    }

    private String developmentCondition;
    private String infectionRoute;
    private String cropName;
    private String sickNameChn;
    private String sickNameKor;
    private String sickNameEng;
    private String chemicalPrvnbeMth;
}
