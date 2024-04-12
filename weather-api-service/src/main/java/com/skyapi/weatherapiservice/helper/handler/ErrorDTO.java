package com.skyapi.weatherapiservice.helper.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorDTO {

    private Date timeStamp;
    private int status;
    private String path;
    @Builder.Default
    private List<String> errors = new ArrayList<>();

    public ErrorDTO() {
        this.errors = new ArrayList<>();
    }

    public void addErrors(String message){
        this.errors.add(message);
    }

}
