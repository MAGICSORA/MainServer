package com.example.servertest.main.nabatbu.diagnosis.repository;

import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisRecord;
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
    List<DiagnosisRecord> findAllByCategoryIdOrderByRegDateDesc(Long categoryId);

    int countDiagnosisRecordByCategoryId(Long categoryId);

    List<DiagnosisRecord> findAllByUserIdOrderByRegDateDesc(Long userId);
}
