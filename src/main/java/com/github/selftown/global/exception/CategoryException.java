package com.github.selftown.global.exception;

import java.util.Map;

public class CategoryException extends CustomException {

    public CategoryException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CategoryException(ErrorCode errorCode, Map<String, String> inputValuesByProperty) {
        super(errorCode, inputValuesByProperty);
    }

    public static class CategoryNotFoundException extends CategoryException {
        public CategoryNotFoundException() {
            super(ErrorCode.INVALID_CATEGORY);
        }

        public CategoryNotFoundException(Map<String, String> inputValuesByProperty) {
            super(ErrorCode.INVALID_CATEGORY, inputValuesByProperty);
        }
    }
}
