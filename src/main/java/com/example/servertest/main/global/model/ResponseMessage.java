package com.example.servertest.main.global.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {

    private String code;
    private String message;
    private Object data;

    public static ResponseMessage fail(String code, String message) {
        return fail(code, message, null);
    }

    public static ResponseMessage fail(String code, String message, Object data) {
        return ResponseMessage.builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static ResponseMessage success(Object data) {
        return ResponseMessage.builder()
                .message("")
                .data(data)
                .build();
    }

    public static ResponseMessage success() {
        return success(null);
    }
}
