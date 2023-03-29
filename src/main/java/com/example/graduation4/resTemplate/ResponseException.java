package com.example.graduation4.resTemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseException extends Exception{ //사용자정의 예외 던지기 위함
    private ResponseTemplateStatus status;
}