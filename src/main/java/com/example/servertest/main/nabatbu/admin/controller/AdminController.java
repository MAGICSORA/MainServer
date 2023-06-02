package com.example.servertest.main.nabatbu.admin.controller;

import com.example.servertest.main.global.model.ResponseResult;
import com.example.servertest.main.global.model.ServiceResult;
import com.example.servertest.main.nabatbu.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/list")
    public ResponseEntity<?> getUserList(@RequestHeader("Authorization") String token,
                                         @RequestParam String email, @RequestParam String name,
                                         @RequestParam int pageNum) {

        ServiceResult result = adminService.getMemberList(token, email, name, pageNum);
        return ResponseResult.result(result);
    }

    @PostMapping("/setAuth")
    public ResponseEntity<?> setUserAuth(@RequestHeader("Authorization") String token,
                                         @RequestParam Long userId, @RequestParam int setLevel) {
        ServiceResult result = adminService.setMemberAuth(token, userId, setLevel);
        return ResponseResult.result(result);
    }
}
