package com.example.servertest.main.crop.repository;

import com.example.servertest.main.crop.entity.CropOccurInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropOccurInfoRepository extends JpaRepository<CropOccurInfo, Long> {

    CropOccurInfo findTopByOrderByIdDesc();

}
