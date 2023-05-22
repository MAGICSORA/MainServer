package com.example.servertest.main.nabatbu.cropInfo.repository;

import com.example.servertest.main.nabatbu.cropInfo.entity.CropOccurInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropOccurInfoRepository extends JpaRepository<CropOccurInfo, Long> {

    CropOccurInfo findTopByOrderByIdDesc();

}
