package com.example.servertest.main.nabatbu.category.repository;

import com.example.servertest.main.nabatbu.diagnosis.entity.DiagnosisRecord;
import com.example.servertest.main.nabatbu.category.entity.MyCropHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyCropHistoryRepository extends JpaRepository<MyCropHistory, Long> {

    List<MyCropHistory> findAllByDiagnosisRecord(DiagnosisRecord diagnosisRecord);
}