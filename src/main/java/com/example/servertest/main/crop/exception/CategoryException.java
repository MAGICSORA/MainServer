package com.example.servertest.main.crop.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class CategoryException extends RuntimeException{

    private CategoryError categoryError;
//    private String error;

    public CategoryException(CategoryError categoryError) {
        super(categoryError.getDescription());
        this.categoryError = categoryError;
    }
}
