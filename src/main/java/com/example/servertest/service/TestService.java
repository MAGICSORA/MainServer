package com.example.servertest.service;

import com.example.servertest.entity.TestEntity;
import com.example.servertest.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    public void test(String input) {
        testRepository.save(TestEntity.builder().content(input).build());
    }
}
