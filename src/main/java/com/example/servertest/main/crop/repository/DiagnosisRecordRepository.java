package com.example.servertest.main.crop.repository;

import com.example.servertest.main.crop.entity.DiagnosisRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRecordRepository extends JpaRepository<DiagnosisRecord, Long> {

    DiagnosisRecord findTopByOrderByIdDesc();

    List<DiagnosisRecord> findAllByUserLatitudeBetween(float latitude1, float latitude2);
    List<DiagnosisRecord> findAllByUserLongitudeBetween(float longitude1, float longitude2);
}
