package com.example.servertest.main.nabatbu.diagnosis.repository;

import com.example.servertest.main.nabatbu.cropInfo.entity.DiseaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseDetailRepository extends JpaRepository<DiseaseDetail, Long> {

}
