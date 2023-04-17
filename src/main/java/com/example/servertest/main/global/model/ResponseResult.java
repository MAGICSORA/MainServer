package com.example.servertest.main.global.model;

import org.springframework.http.ResponseEntity;

public class ResponseResult {

    public static ResponseEntity<?> fail(String code, String message) {
        return ResponseEntity.badRequest().body(ResponseMessage.fail(code, message));
    }

    public static ResponseEntity<?> success() {
        return success(null);
    }

    public static ResponseEntity<?> result(ServiceResult result) {
        if (result.isFail()) {
            return fail(result.getCode(), result.getMessage());
        }
        return success(result);
    }

    public static ResponseEntity<?> success(ServiceResult result) {
        return ResponseEntity.ok(ResponseMessage.success(result.getObject()));
    }
}
