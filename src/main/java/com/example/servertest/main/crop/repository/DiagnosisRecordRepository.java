package com.example.servertest.main.crop.repository;

import com.example.servertest.main.crop.entity.DiagnosisRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosisRecordRepository extends JpaRepository<DiagnosisRecord, Long> {

    DiagnosisRecord findTopByOrderById();

    DiagnosisRecord findTopByOrderByIdDesc();
}
