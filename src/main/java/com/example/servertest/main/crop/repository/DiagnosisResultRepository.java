package com.example.servertest.main.crop.repository;

import com.example.servertest.main.crop.entity.DiagnosisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosisResultRepository extends JpaRepository<DiagnosisResult, Long> {

}
