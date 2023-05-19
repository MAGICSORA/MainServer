package com.example.servertest.main.crop.service;

import com.example.servertest.main.crop.entity.DiagnosisRecord;
import com.example.servertest.main.crop.entity.MyCrop;
import com.example.servertest.main.crop.repository.DiagnosisRecordRepository;
import com.example.servertest.main.crop.repository.MyCropRepository;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.member.entity.Member;
import com.example.servertest.main.member.exception.MemberError;
import com.example.servertest.main.member.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyCropService {

    private final MemberService memberService;

    private final MyCropRepository myCropRepository;
    private final DiagnosisRecordRepository diagnosisRecordRepository;

    public ServiceResult registerContents(String token, Long diagnosisId, String contents) {

        Member member = new Member();
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
//            e.printStackTrace();
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Optional<DiagnosisRecord> optionalDiagnosisRecord = diagnosisRecordRepository.findById(diagnosisId);
        DiagnosisRecord diagnosisRecord = optionalDiagnosisRecord.get();

        MyCrop myCrop = MyCrop.builder()
                .diagnosisRecord(diagnosisRecord)
                .regDt(LocalDateTime.now())
                .updateDt(LocalDateTime.now())
                .contents(contents)
                .build();

        myCropRepository.save(myCrop);
        return ServiceResult.success(myCrop);
    }
}
