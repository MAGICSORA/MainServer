package com.example.servertest.main.crop.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class NcpmsException extends RuntimeException{

    private NcpmsError ncpmsError;
//    private String error;

    public NcpmsException(NcpmsError ncpmsError) {
        super(ncpmsError.getDescription());
        this.ncpmsError = ncpmsError;
    }
}
