package com.example.servertest.main.crop.repository;

import com.example.servertest.main.crop.entity.DiagnosisRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRecordRepository extends JpaRepository<DiagnosisRecord, Long> {

//    List<DiagnosisRecord> findBy

    DiagnosisRecord findTopByOrderByIdDesc();
    List<DiagnosisRecord> findAllByUserLatitudeBetween(double latitude1, double latitude2);
    List<DiagnosisRecord> findAllByUserLongitudeBetween(double longitude1, double longitude2);
    List<DiagnosisRecord> findAllByCategoryId(Long categoryId);

    int countDiagnosisRecordByCategoryId(Long categoryId);
}
