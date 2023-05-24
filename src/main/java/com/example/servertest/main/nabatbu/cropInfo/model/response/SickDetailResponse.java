package com.example.servertest.main.nabatbu.cropInfo.model.response;

import com.example.servertest.main.nabatbu.cropInfo.ncpms.model.response.NcpmsSickDetailService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class SickDetailResponse {

    private String developmentCondition;
    private String symptoms;
    private String preventionMethod;
    private String infectionRoute;
    private List<Img> imageList;

    public static SickDetailResponse from(NcpmsSickDetailService item) {
        List<NcpmsSickDetailService.ImageList.Item2> item1 = item.getImageList().getItem();
        List<Img> imgList = new ArrayList<>();
        for (NcpmsSickDetailService.ImageList.Item2 item2 : item1) {
            imgList.add(Img.builder().imageTitle(item2.getImageTitle()).imagePath(item2.getImage()).build());
        }

        SickDetailResponse response = SickDetailResponse.builder()
                .developmentCondition(item.getDevelopmentCondition())
                .symptoms(item.getSymptoms())
                .preventionMethod(item.getPreventionMethod())
                .infectionRoute(item.getInfectionRoute())
                .imageList(imgList)
                .build();

        return response;
    }
}
