package com.example.servertest.main.crop.repository;

import com.example.servertest.main.crop.entity.DiagnosisRecord;
import com.example.servertest.main.crop.entity.MyCropHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyCropHistoryRepository extends JpaRepository<MyCropHistory, Long> {

    List<MyCropHistory> findAllByDiagnosisRecord(DiagnosisRecord diagnosisRecord);
}