package com.eltimo.tasknest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDTO {

    private int status;
    private String message;
    private LocalDateTime timestamp;

}
