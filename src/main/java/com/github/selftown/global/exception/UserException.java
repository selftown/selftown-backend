package com.github.selftown.global.exception;

import java.util.Map;

public class UserException extends CustomException{

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, Map<String, String> inputValuesByProperty) {
        super(errorCode, inputValuesByProperty);
    }

    public static class UserNotFountException extends UserException {
        public UserNotFountException() {
            super(ErrorCode.USER_NOT_FOUND);
        }

        public UserNotFountException(Map<String, String> inputValuesByProperty) {
            super(ErrorCode.USER_NOT_FOUND, inputValuesByProperty);
        }

    }
}
