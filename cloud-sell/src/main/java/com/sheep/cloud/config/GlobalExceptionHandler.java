package com.sheep.cloud.config;

import com.sheep.cloud.dto.response.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/11/2 星期三
 * Happy Every Coding Time~
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ApiResult handleException(Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ee = (MethodArgumentNotValidException) e;
            BindingResult bindingResult = ee.getBindingResult();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            StringBuilder sb = new StringBuilder();
            for (ObjectError allError : allErrors) {
                FieldError fieldError = (FieldError) allError;
                sb.append(fieldError.getDefaultMessage()).append(";");
            }
            return new ApiResult<>().error(sb.toString());
        } else {
            return new ApiResult<>().error(e.getMessage());
        }
    }
}
