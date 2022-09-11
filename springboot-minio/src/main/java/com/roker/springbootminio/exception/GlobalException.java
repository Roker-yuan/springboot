package com.roker.springbootminio.exception;

import com.roker.springbootminio.domain.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = Exception.class)
    public R handle(Exception e) {
        return R.error(e.getMessage());
    }
}
