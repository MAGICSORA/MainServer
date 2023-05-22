package com.example.servertest.main.nabatbu.category.model;

import com.example.servertest.main.nabatbu.category.entity.Category;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CategoryResponse {

    private long id;
    private long userId;
    private String name;
    private int cnt;
    private String memo;
    private LocalDateTime regDt;

    public static CategoryResponse to(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .userId(category.getUserId())
                .name(category.getName())
                .memo(category.getMemo())
                .regDt(category.getRegDt())
                .build();
    }
}
