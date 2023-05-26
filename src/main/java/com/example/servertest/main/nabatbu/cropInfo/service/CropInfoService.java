package com.example.servertest.main.nabatbu.cropInfo.service;

import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.cropInfo.entity.SickList;
import com.example.servertest.main.nabatbu.cropInfo.exception.NcpmsError;
import com.example.servertest.main.nabatbu.cropInfo.exception.NcpmsException;
import com.example.servertest.main.nabatbu.cropInfo.model.response.SickDetailResponse;
import com.example.servertest.main.nabatbu.cropInfo.model.response.SickListResponse;
import com.example.servertest.main.nabatbu.cropInfo.ncpms.model.response.NcpmsSickDetailService;
import com.example.servertest.main.nabatbu.cropInfo.ncpms.service.NcpmsService;
import com.example.servertest.main.nabatbu.cropInfo.repository.SickListRepository;
import com.example.servertest.main.nabatbu.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CropInfoService {

    private final MemberService memberService;
    private final NcpmsService ncpmsService;

    private final SickListRepository sickListRepository;

    public ServiceResult sickList(String token, String cropName, String sickNameKor, int displayCount, int startPoint) {

        if (memberService.checkToken(token).isFail()) {
            return memberService.checkToken(token);
        }

        PageRequest page = PageRequest.of(startPoint, displayCount);
        Slice<SickList> sickList = sickListRepository.findByCropNameContainingAndSickNameKorContaining(cropName, sickNameKor, page);
        int count = sickListRepository.countByCropNameContainingAndSickNameKorContaining(cropName, sickNameKor);

        return ServiceResult.success(SickListResponse.builder().sickList(sickList.getContent()).totalCnt(count).build());
    }

    public ServiceResult sickDetail(String token, String urlBuilder) {

        if (memberService.checkToken(token).isFail()) {
            return memberService.checkToken(token);
        }

        ServiceResult result;
        try {
            Map<String, NcpmsSickDetailService> output = (Map<String, NcpmsSickDetailService>) ncpmsService.returnResult(urlBuilder, false).getObject();
            NcpmsSickDetailService ncpmsSickDetailService = output.get("response");
            result = ServiceResult.success(SickDetailResponse.from(ncpmsSickDetailService));
        } catch (Exception e) {
            NcpmsException exception = new NcpmsException(NcpmsError.NO_DATA_EXISTS);
            result = ServiceResult.fail(String.valueOf(exception.getNcpmsError()), exception.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}
