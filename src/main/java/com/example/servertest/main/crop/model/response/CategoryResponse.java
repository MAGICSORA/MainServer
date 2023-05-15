package com.example.servertest.main.crop.model.response;

import com.example.servertest.main.crop.entity.Category;
import com.example.servertest.main.crop.repository.CategoryRepository;
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
    private LocalDateTime regDt;

    public static CategoryResponse to(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .userId(category.getUserId())
                .name(category.getName())
                .regDt(category.getRegDt())
                .build();
    }
}
