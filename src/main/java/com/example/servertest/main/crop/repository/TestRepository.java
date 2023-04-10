package com.example.servertest.main.crop.repository;

import com.example.servertest.main.crop.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {

    TestEntity findByContent(String input);
}
