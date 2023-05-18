package com.example.servertest.main.api.psis.model.response;

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

    private String pestiKorName;
    private String useName;
    private String compName;
    private String pestiBrandName;
    private String pestiEngName;
    private String regCpntQnty;
    private String toxicGubun;
    private String toxicName;
    private String fishToxicGubun;
    private String cropName;
    private String diseaseWeedName;
    private String pestiUse;
    private String dilutUnit;
    private String useSuittime;
    private String useNum;
}
