package com.task.polynomial.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluateRequest {

    @Schema(
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "-5+4*x+3*x^2",
            description = "Simplified polynomial")
    @NotNull
    @Pattern(regexp = "^[-+]?(\\(?[-+]?(\\d+(\\.\\d+)?|\\d*\\.\\d+)?(\\*?x(\\^\\d+)?)?([-+]\\s*(\\(?[-+]?(\\d+(\\.\\d+)?|\\d*\\.\\d+)?(\\*?x(\\^\\d+)?)?\\)?)?)*\\)?)(\\*\\(?[-+]?(\\d+(\\.\\d+)?|\\d*\\.\\d+)?(\\*?x(\\^\\d+)?)?([-+]\\s*(\\(?[-+]?(\\d+(\\.\\d+)?|\\d*\\.\\d+)?(\\*?x(\\^\\d+)?)?\\)?)?)*\\)?)?$",
            message = "Wrong format for polynomial")
    private String polynomial;

    @Schema(
            implementation = Integer.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "5",
            description = "Value for variable x")
    @NotNull
    private Integer x;
}
