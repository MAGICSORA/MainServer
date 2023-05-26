package com.example.servertest.main.nabatbu.cropInfo.repository;

import com.example.servertest.main.nabatbu.cropInfo.entity.SickList;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SickListRepository extends JpaRepository<SickList, String> {

    Slice<SickList> findByCropNameContainingAndSickNameKorContaining(
            String cropName, String sickName, Pageable pageable);

    int countByCropNameContainingAndSickNameKorContaining (
            String cropName, String sickName);

    SickList findBySickNameKorAndCropName(String sickNameKor, String cropName);

    SickList findBySickKey(String sickKey);
}
