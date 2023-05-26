package com.example.servertest.main.nabatbu.cropInfo.model.response;

import com.example.servertest.main.nabatbu.cropInfo.entity.SickList;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
public class SickListResponse {

    private int totalCnt;
    private List<SickList> sickList;
}
