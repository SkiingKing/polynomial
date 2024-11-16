package com.task.polynomial.service;

import com.task.polynomial.domain.EvaluateRequest;
import com.task.polynomial.domain.SimplifiedRequest;

public interface PolynomialOperation {

    String simplify(SimplifiedRequest simplifiedRequest);

    Integer evaluate(EvaluateRequest evaluateRequest);
}
