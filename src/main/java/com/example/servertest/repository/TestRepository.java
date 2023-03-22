package com.example.servertest.repository;

import com.example.servertest.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {

    TestEntity findByContent(String input);
}
