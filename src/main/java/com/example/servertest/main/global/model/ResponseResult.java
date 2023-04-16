package com.example.servertest.main.global.model;

import org.springframework.http.ResponseEntity;

public class ResponseResult {

    public static ResponseEntity<?> fail(String message) {
        return ResponseEntity.badRequest().body(ResponseMessage.fail(message));
    }

    public static ResponseEntity<?> success() {
        return success(null);
    }

    public static ResponseEntity<?> result(ServiceResult result) {
        if (result.isFail()) {
            return fail(result.getMessage());
        }
        return success(result);
    }

    public static ResponseEntity<?> success(Object data) {
        return ResponseEntity.ok(ResponseMessage.success(data));
    }
}
