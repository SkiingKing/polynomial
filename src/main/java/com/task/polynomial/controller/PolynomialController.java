package com.task.polynomial.controller;

import com.task.polynomial.domain.EvaluateRequest;
import com.task.polynomial.domain.SimplifiedRequest;
import com.task.polynomial.exeption.ErrorResponse;
import com.task.polynomial.service.PolynomialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/polynomial")
@RequiredArgsConstructor
@Log4j2
public class PolynomialController {

    private final PolynomialService polynomialService;


    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(summary = "Simplify polynomial",
            description = "Simplify polynomial. The response is simplified polynomial."
    )
    @PostMapping("/simplify")
    public String simplifiedPolynomial(@Valid @RequestBody SimplifiedRequest polynomial) {
        return polynomialService.simplify(polynomial);
    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "400", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(summary = "Evaluate polynomial",
            description = "Evaluate polynomial. If the user enters a value for x, the response will contain the result of the polynomial evaluation."
    )
    @PostMapping("/evaluate")
    public Integer evaluatePolynomial(@Valid @RequestBody EvaluateRequest evaluateRequest) {
        return polynomialService.evaluate(evaluateRequest);
    }
}
