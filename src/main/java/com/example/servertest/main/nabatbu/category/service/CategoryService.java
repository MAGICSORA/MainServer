package com.example.servertest.main.nabatbu.category.service;

import com.example.servertest.main.nabatbu.category.entity.Category;
import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisRecord;
import com.example.servertest.main.nabatbu.category.exception.CategoryError;
import com.example.servertest.main.nabatbu.category.exception.CategoryException;
import com.example.servertest.main.nabatbu.category.model.CategoryResponse;
import com.example.servertest.main.nabatbu.category.repository.CategoryRepository;
import com.example.servertest.main.nabatbu.diagnosis.repository.DiagnosisRecordRepository;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.member.entity.Member;
import com.example.servertest.main.nabatbu.member.exception.MemberError;
import com.example.servertest.main.nabatbu.member.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final MemberService memberService;
    private final CategoryRepository categoryRepository;
    private final DiagnosisRecordRepository diagnosisRecordRepository;

    public ServiceResult registerCategory(String name, String token) {

        Member member = new Member();
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        if (name.length() > 20) {
            CategoryError error = CategoryError.INVALID_NAME_LENGTH;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Category optionalCategory = categoryRepository.findByNameAndUserId(name, member.getId());
        if (optionalCategory != null) {
            CategoryException e = new CategoryException(CategoryError.CATEGORY_ALREADY_EXIST);
            return ServiceResult.fail(String.valueOf(e.getCategoryError()), e.getMessage());
        }

        Category category = Category.builder()
                .userId(member.getId())
                .name(name)
                .regDt(LocalDateTime.now())
                .memo("")
                .build();

        categoryRepository.save(category);

        return ServiceResult.success(category);
    }

    public ServiceResult getCategoryList(String token) {

        Member member = new Member();
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        List<Category> categoryList = categoryRepository.findAllByUserId(member.getId());
        List<CategoryResponse> categoryResponseList = new ArrayList<>();
        for (Category item : categoryList) {
            CategoryResponse categoryResponse = CategoryResponse.to(item);
            categoryResponse.setCnt(diagnosisRecordRepository.countDiagnosisRecordByCategoryId(item.getId()));
            categoryResponseList.add(categoryResponse);
        }

        return ServiceResult.success(categoryResponseList);
    }

    public ServiceResult getOneCategory(String token, String name) {

        Member member = new Member();
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Category category = categoryRepository.findByNameAndUserId(name, member.getId());
        if (category == null) {
            CategoryError error = CategoryError.CATEGORY_NOT_FOUND;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        } else {
            CategoryResponse categoryResponse = CategoryResponse.to(category);
            categoryResponse.setCnt(diagnosisRecordRepository.countDiagnosisRecordByCategoryId(category.getId()));
            return ServiceResult.success(categoryResponse);
        }
    }

    public ServiceResult updateCategory(Long id, String changeName, String changeMemo, String token) {

        Member member = new Member();
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        Optional<Category> optionalCategory = categoryRepository.findById(id);
        Category category = optionalCategory.get();
//        Category category = categoryRepository.findByNameAndUserId(originalName, member.getId());

        //제목 길이제한
        if (changeName.length() > 20 || changeName.length() < 1) {
            CategoryError error = CategoryError.INVALID_NAME_LENGTH;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        if(!category.getName().equals(changeName)) {
            Category category2 = categoryRepository.findByNameAndUserId(changeName, member.getId());
            if (category2 != null) {
                CategoryException e = new CategoryException(CategoryError.CATEGORY_ALREADY_EXIST);
                return ServiceResult.fail(String.valueOf(e.getCategoryError()), e.getMessage());
            }
        }

        if (changeMemo.length() > 200) {
            CategoryError error = CategoryError.INVALID_MEMO_LENGTH;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        category.setName(changeName);
        category.setRegDt(LocalDateTime.now());
        category.setMemo(changeMemo);

        categoryRepository.save(category);

        return ServiceResult.success(category);
    }

    public ServiceResult deleteCategory(String token, Long categoryId) {

        Member member = new Member();
        try {
            member = memberService.validateMember(token);
        } catch (ExpiredJwtException e) {
            MemberError error = MemberError.EXPIRED_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        } catch (Exception e) {
            MemberError error = MemberError.INVALID_TOKEN;
            return ServiceResult.fail(String.valueOf(error), error.getDescription());
        }

        List<DiagnosisRecord> diagnosisRecordList = diagnosisRecordRepository.findAllByCategoryId(categoryId);
        if (diagnosisRecordList.size()!=0) {
            for (DiagnosisRecord item : diagnosisRecordList) {
                item.setCategoryId(0);
                diagnosisRecordRepository.save(item);
            }
        }
        Category category = categoryRepository.findByIdAndUserId(categoryId, member.getId());
        categoryRepository.delete(category);

        return ServiceResult.success();
    }

    public ServiceResult updateRecordCategory(String token, Long recordId, Long categoryId) {

        if (memberService.checkToken(token).isFail()) {
            return memberService.checkToken(token);
        }
        Optional<DiagnosisRecord> optionalDiagnosisRecord = diagnosisRecordRepository.findById(recordId);
        DiagnosisRecord diagnosisRecord = optionalDiagnosisRecord.get();
        diagnosisRecord.setCategoryId(categoryId);
        diagnosisRecordRepository.save(diagnosisRecord);

        return ServiceResult.success();
    }

    public ServiceResult getDiagnosisOfCategory(String token, Long categoryId) {

        if (memberService.checkToken(token).isFail()) {
            return memberService.checkToken(token);
        }
        List<DiagnosisRecord> diagnosisRecordList = diagnosisRecordRepository.findAllByCategoryIdOrderByRegDateDesc(categoryId);


        return ServiceResult.success(diagnosisRecordList);
    }
}