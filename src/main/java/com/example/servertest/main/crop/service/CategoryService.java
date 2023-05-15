package com.example.servertest.main.crop.service;

import com.example.servertest.main.crop.entity.Category;
import com.example.servertest.main.crop.entity.DiagnosisRecord;
import com.example.servertest.main.crop.exception.CategoryError;
import com.example.servertest.main.crop.exception.CategoryException;
import com.example.servertest.main.crop.exception.CropError;
import com.example.servertest.main.crop.exception.CropException;
import com.example.servertest.main.crop.model.response.CategoryResponse;
import com.example.servertest.main.crop.repository.CategoryRepository;
import com.example.servertest.main.crop.repository.DiagnosisRecordRepository;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.member.entity.Member;
import com.example.servertest.main.member.exception.MemberException;
import com.example.servertest.main.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final MemberService memberService;
    private final CategoryRepository categoryRepository;
    private final DiagnosisRecordRepository diagnosisRecordRepository;

    public ServiceResult registerCategory(String name, String token) {

        Member member;
        try {
            member = memberService.validateMember(token);
        } catch (MemberException e) {
            return ServiceResult.fail(String.valueOf(e.getMemberError()), e.getMessage());
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
                .build();

        categoryRepository.save(category);

        return ServiceResult.success(category);
    }

    public ServiceResult getCategoryList(String token) {

        Member member;
        try {
            member = memberService.validateMember(token);
        } catch (MemberException e) {
            return ServiceResult.fail(String.valueOf(e.getMemberError()), e.getMessage());
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

    public ServiceResult updateCategory(String originalName, String changeName, String token) {

        Member member;
        try {
            member = memberService.validateMember(token);
        } catch (MemberException e) {
            return ServiceResult.fail(String.valueOf(e.getMemberError()), e.getMessage());
        }

        Category category = categoryRepository.findByNameAndUserId(originalName, member.getId());

        Category category2 = categoryRepository.findByNameAndUserId(changeName, member.getId());
        if (category2 != null) {
            CategoryException e = new CategoryException(CategoryError.CATEGORY_ALREADY_EXIST);
            return ServiceResult.fail(String.valueOf(e.getCategoryError()), e.getMessage());
        }

        category.setName(changeName);
        category.setRegDt(LocalDateTime.now());
        categoryRepository.save(category);

        return ServiceResult.success(category);
    }

    public ServiceResult deleteCategory(String token, Long categoryId) {

        Member member;
        try {
            member = memberService.validateMember(token);
        } catch (MemberException e) {
            return ServiceResult.fail(String.valueOf(e.getMemberError()), e.getMessage());
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
}
