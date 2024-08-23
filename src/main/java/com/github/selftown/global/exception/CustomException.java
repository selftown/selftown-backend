package com.github.selftown.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@ToString
public class CustomException extends RuntimeException {

    private final int code;
    private final String message;
    private final Map<String, String> inputValuesByProperty;

    static class ErrorConstant {
        static final String EXCEPTION_INFO_BRACKET = "{ %s | %s }";
        static final String CODE_MESSAGE = "Code: %d, Message %s ";
        static final String PROPERTY_VALUE = "Property : %s, Value: %s ";
        static final String VALUE_DELIMITER = "/";
    }

    protected CustomException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.inputValuesByProperty = Map.of();
    }

    protected CustomException(ErrorCode errorCode, Map<String, String> inputValuesByProperty) {

        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.inputValuesByProperty = inputValuesByProperty;
    }

    public String getErrorInfoLog() {
        String codeMessage = String.format(ErrorConstant.CODE_MESSAGE, code, message);
        String errorPropertyValue = getErrorPropertyValue();

        return String.format(ErrorConstant.EXCEPTION_INFO_BRACKET, codeMessage, errorPropertyValue);
    }

    private String getErrorPropertyValue() {

        return inputValuesByProperty.entrySet()
                .stream()
                .map(entry -> String.format(ErrorConstant.PROPERTY_VALUE, entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(ErrorConstant.VALUE_DELIMITER));
    }
}
