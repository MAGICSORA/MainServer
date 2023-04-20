package com.example.servertest.main.crop.repository;

import com.example.servertest.main.crop.entity.DiseaseDetail;
import com.example.servertest.main.crop.entity.SickList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseDetailRepository extends JpaRepository<DiseaseDetail, Long> {

}
