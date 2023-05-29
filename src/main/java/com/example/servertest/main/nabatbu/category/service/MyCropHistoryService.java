package com.example.servertest.main.nabatbu.category.service;

import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisRecord;
import com.example.servertest.main.nabatbu.category.entity.MyCropHistory;
import com.example.servertest.main.nabatbu.diagnosis.repository.DiagnosisRecordRepository;
import com.example.servertest.main.nabatbu.category.repository.MyCropHistoryRepository;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.member.entity.Member;
import com.example.servertest.main.nabatbu.member.exception.MemberError;
import com.example.servertest.main.nabatbu.member.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyCropHistoryService {

    private final MemberService memberService;

    private final MyCropHistoryRepository myCropHistoryRepository;
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

        MyCropHistory myCropHistory = MyCropHistory.builder()
                .diagnosisRecord(diagnosisRecord)
                .regDt(LocalDateTime.now())
                .updateDt(LocalDateTime.now())
                .contents(contents)
                .build();

        myCropHistoryRepository.save(myCropHistory);
        return ServiceResult.success(myCropHistory);
    }

    public ServiceResult contentsList(String token, Long diagnosisRecordId) {

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

        Optional<DiagnosisRecord> optionalDiagnosisRecord = diagnosisRecordRepository.findById(diagnosisRecordId);
        DiagnosisRecord diagnosisRecord = optionalDiagnosisRecord.get();

        List<MyCropHistory> myCropHistoryList = myCropHistoryRepository.findAllByDiagnosisRecordOrderByUpdateDtDesc(diagnosisRecord);

        return ServiceResult.success(myCropHistoryList);
    }

    public ServiceResult contentsDetail(String token, Long myCropId) {

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

        Optional<MyCropHistory> optionalMyCrop = myCropHistoryRepository.findById(myCropId);
        MyCropHistory myCropHistory = optionalMyCrop.get();

        return ServiceResult.success(myCropHistory);
    }

    public ServiceResult updateMemo(String token, Long myCropId, String contents) {

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

        Optional<MyCropHistory> optionalMyCrop = myCropHistoryRepository.findById(myCropId);
        MyCropHistory myCropHistory = optionalMyCrop.get();

        myCropHistory.setContents(contents);
        myCropHistory.setUpdateDt(LocalDateTime.now());

        myCropHistoryRepository.save(myCropHistory);

        return ServiceResult.success(myCropHistory);
    }

    public ServiceResult deleteMemo(String token, Long myCropId) {

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

        Optional<MyCropHistory> optionalMyCrop = myCropHistoryRepository.findById(myCropId);
        MyCropHistory myCropHistory = optionalMyCrop.get();

        myCropHistoryRepository.delete(myCropHistory);

        return ServiceResult.success();
    }
}
