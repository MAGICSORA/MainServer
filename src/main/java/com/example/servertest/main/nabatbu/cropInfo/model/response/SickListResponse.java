package com.example.servertest.main.nabatbu.cropInfo.model.response;

import com.example.servertest.main.nabatbu.cropInfo.entity.SickList;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SickListResponse {

    private List<SickList> sickList;
    private int cnt;
}
