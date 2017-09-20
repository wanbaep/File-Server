package com.wanbaep.utils;

import lombok.Getter;
import org.springframework.dao.EmptyResultDataAccessException;

@Getter
public class JdbcEmptyResultException extends RuntimeException {
    private Long id;
    private String message;
    private EmptyResultDataAccessException emptyResultDataAccessException;

    public JdbcEmptyResultException(EmptyResultDataAccessException exception, String message, Long id) {
        super();
        this.emptyResultDataAccessException = exception;
        this.message = message;
        this.id = id;
    }

}
