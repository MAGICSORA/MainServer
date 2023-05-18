package com.example.servertest.main.crop.repository;

import com.example.servertest.main.crop.entity.DiseaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseDetailRepository extends JpaRepository<DiseaseDetail, Long> {

}
