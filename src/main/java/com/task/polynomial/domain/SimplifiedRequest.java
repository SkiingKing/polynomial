package com.task.polynomial.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimplifiedRequest {

    @Schema(
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "2*x^2+3*x-5+x^2+x",
            description = "Polynomial")
    @NotNull
    @Pattern(regexp = "^[-+]?(\\(?[-+]?(\\d+(\\.\\d+)?|\\d*\\.\\d+)?(\\*?x(\\^\\d+)?)?([-+]\\s*(\\(?[-+]?(\\d+(\\.\\d+)?|\\d*\\.\\d+)?(\\*?x(\\^\\d+)?)?\\)?)?)*\\)?)(\\*\\(?[-+]?(\\d+(\\.\\d+)?|\\d*\\.\\d+)?(\\*?x(\\^\\d+)?)?([-+]\\s*(\\(?[-+]?(\\d+(\\.\\d+)?|\\d*\\.\\d+)?(\\*?x(\\^\\d+)?)?\\)?)?)*\\)?)?$",
            message = "Wrong format for polynomial")
    private String polynomial;
}
