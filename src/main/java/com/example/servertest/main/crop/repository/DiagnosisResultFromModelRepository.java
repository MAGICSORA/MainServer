package com.example.servertest.main.crop.repository;

import com.example.servertest.main.crop.entity.DiagnosisResultFromModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosisResultFromModelRepository extends JpaRepository<DiagnosisResultFromModel, Long> {

}
