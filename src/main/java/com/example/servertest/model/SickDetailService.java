package com.example.servertest.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "service")
@Getter
@Setter
public class SickDetailService {

    @XmlElement(name = "virusImgList")
    private VirusImgList virusImgList;

    @Getter
    @Setter
    @XmlRootElement(name = "virusImgList")
    private static class VirusImgList {

        @XmlElement(name = "etc")
        private String etc;

        @XmlElement(name = "biologyPrvnbeMth")
        private String biologyPrvnbeMth;

        @XmlElement(name = "preventionMethod")
        private String preventionMethod;

        @XmlElement(name = "symptoms")
        private String symptoms;

        @XmlElement(name = "imageList")
        private ImageList imageList;

        @Getter
        @Setter
        @XmlRootElement(name = "imageList")
        private static class ImageList {

            private List<Item> item;

            @Getter
            @Setter
            @XmlRootElement(name = "item")
            private static class Item {

                private String imageTitle;
                private String iemSpchcknNm;
                private String image;
                private String iemSpchcknCode;
            }
        }
    }

    @XmlElement(name = "developmentCondition")
    private String developmentCondition;

    @XmlElement(name = "infectionRoute")
    private String infectionRoute;

    @XmlElement(name = "cropName")
    private String cropName;

    @XmlElement(name = "sickNameChn")
    private String sickNameChn;

    @XmlElement(name = "sickNameKor")
    private String sickNameKor;

    @XmlElement(name = "sickNameEng")
    private String sickNameEng;

    @XmlElement(name = "chemicalPrvnbeMth")
    private String chemicalPrvnbeMth;
}
