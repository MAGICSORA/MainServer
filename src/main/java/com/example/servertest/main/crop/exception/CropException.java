package com.example.servertest.main.crop.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class CropException extends RuntimeException{

    private CropError cropError;
//    private String error;

    public CropException(CropError cropError) {
        super(cropError.getDescription());
        this.cropError = cropError;
    }
}
