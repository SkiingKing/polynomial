package com.task.polynomial.exeption;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String message;

    private List<String> details;
}
