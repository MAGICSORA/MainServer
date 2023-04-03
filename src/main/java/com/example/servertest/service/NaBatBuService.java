package com.example.servertest.service;

import com.example.servertest.entity.SickList;
import com.example.servertest.model.SickListDto;
import com.example.servertest.repository.SickListRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaBatBuService {

    private final SickListRepository sickListRepository;

    public SickList saveSickList(SickListDto sickListDto) {
        SickList sickList = SickList.builder()
            .sickKey(sickListDto.getSickKey())
            .cropName(sickListDto.getCropName())
            .sickNameEng(sickListDto.getSickNameEng())
            .sickNameKor(sickListDto.getSickNameKor())
            .build();

        return sickListRepository.save(sickList);
    }

    public List<SickList> getSickList(String cropName, String sickNameKor) {
        List<SickList> sickListDtoList = sickListRepository.findByCropNameContainingAndSickNameKorContaining(
            cropName, sickNameKor);
        return sickListDtoList;
    }
}
