package com.example.servertest.controller;

import com.example.servertest.entity.TestEntity;
import com.example.servertest.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/save")
    public void testDb(@RequestParam String input) {

        testService.test(input);
    }

    @GetMapping("/hi")
    public String test(){
        return "hi";
    }

    @GetMapping("/zz")
    public String sec(){
        return "남이경 호구";
    }

    @GetMapping("/showall")
    public List showAll(){
        return testService.showAll();
    }

    @GetMapping("delete")
    public void delete(@RequestParam String input){
        testService.deleteTest(input);
    }
}
