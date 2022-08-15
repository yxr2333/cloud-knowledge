package com.sheep.cloud.config;

import com.sheep.cloud.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.config
 * @datetime 2022/8/12 星期五
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ApiResult exceptionHanlder(Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            log.error("参数校验失败：{}", Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());
            return ApiResult.error(exception.getBindingResult().getFieldError().getDefaultMessage());
        } else {
            return ApiResult.error(e.getMessage());
        }
    }
}
