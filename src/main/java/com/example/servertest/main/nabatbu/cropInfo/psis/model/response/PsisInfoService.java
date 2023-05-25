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
public class PsisInfoService {

    private String pestiKorName; //품목명
    private String useName; //용도
    private String compName; //법인명
    private String pestiBrandName; //상표명
    private String pestiEngName; //주성분(일반명)
    private String regCpntQnty; //주성분 함량
    private String toxicGubun; //독성 구분코드
    private String toxicName; //독성
    private String fishToxicGubun; //어독성
    private String cropName; //작물명
    private String diseaseWeedName; //병해충명, 잡초명
    private String pestiUse; //사용방법
    private String dilutUnit; //희석배수(10a당 사용량)
    private String useSuittime; //안전사용기준(수확~일전)
    private String useNum; //안전사용기준(~회 이내)
}
