package com.example.servertest.main.test.service;

import com.example.servertest.main.test.entity.TestEntity;
import com.example.servertest.main.test.repository.TestRepository;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public void test(String input) {
        testRepository.save(TestEntity.builder().content(input).build());
    }

    public List showAll() {
        List<TestEntity> testEntityList = testRepository.findAll();
        List list = new ArrayList();
        for (TestEntity test : testEntityList) {
            list.add(test.getContent());
        }
        return list;
    }

    public void deleteTest(String input) {
        TestEntity test = testRepository.findByContent(input);
        testRepository.delete(test);
    }
}
