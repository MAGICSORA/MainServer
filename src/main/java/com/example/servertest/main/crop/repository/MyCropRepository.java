package com.example.servertest.main.crop.repository;

import com.example.servertest.main.crop.entity.MyCrop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyCropRepository extends JpaRepository<MyCrop, Long> {

}