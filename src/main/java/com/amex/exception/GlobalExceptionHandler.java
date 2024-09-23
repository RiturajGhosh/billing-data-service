package com.amex.exception;

import com.amex.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;



@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final int FALLBACK_ERROR_CODE = 900;
    public static final String FALLBACK_ERROR_MSG = "Error occurred please try again or contact with admin";
    private static final Pattern ENUM_MSG = Pattern.compile("values accepted for Enum class");


    @ExceptionHandler(BillingServiceException.class)
    public ResponseEntity handleBusinessException(BillingServiceException ex) {
        var errorResponse = new ErrorResponse(List.of(parseError(ex.getMessage())));
        return new ResponseEntity<>(Optional.of(errorResponse), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception ex) {
        log.error("Exception", ex);
        return new ResponseEntity(Optional.of(Collections.singletonList(new ErrorResponse.Error(FALLBACK_ERROR_CODE,
                FALLBACK_ERROR_MSG))), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ErrorResponse.Error parseError(String errorMessage) {
        try {
            if (StringUtils.isNotBlank(errorMessage)) {
                log.info(errorMessage);
                var message = errorMessage.split("##");
                var errorCode = message.length > 1 ? Integer.parseInt(message[1]) : FALLBACK_ERROR_CODE;
                return new ErrorResponse.Error(errorCode, message[0]);
            }
        } catch (Exception ex) {
            log.error("Internal error", ex);
            return new ErrorResponse.Error(FALLBACK_ERROR_CODE, FALLBACK_ERROR_MSG);
        }
        return new ErrorResponse.Error(FALLBACK_ERROR_CODE, FALLBACK_ERROR_MSG);
    }

}
