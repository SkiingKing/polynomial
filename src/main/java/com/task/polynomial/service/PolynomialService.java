package com.task.polynomial.service;

import com.task.polynomial.domain.EvaluateRequest;
import com.task.polynomial.domain.SimplifiedRequest;
import com.task.polynomial.model.EvaluationCache;
import com.task.polynomial.model.PolynomialCache;
import com.task.polynomial.repository.EvaluationRepository;
import com.task.polynomial.repository.PolynomialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.parser.client.SyntaxError;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.task.polynomial.utils.HashingUtils.generateNumericHash;

@Service
@RequiredArgsConstructor
@Log4j2
public class PolynomialService implements PolynomialOperation {

    private final PolynomialRepository polynomialRepository;
    private final EvaluationRepository evaluationRepository;

    @Override
    public String simplify(SimplifiedRequest simplifiedRequest) {
        Long polynomialId = generateNumericHash(simplifiedRequest.getPolynomial());

        Optional<PolynomialCache> polynomialCache = polynomialRepository.findById(polynomialId);

        if (polynomialCache.isPresent()) {
            log.info("Polynomial was found in cache");
            return polynomialCache.get().getSimplifiedExpression();
        }

        log.info("Polynomial not found in cache. Simplifying and calculating...");
        return processNotCachedPolynomial(simplifiedRequest, polynomialId);
    }

    private String processNotCachedPolynomial(SimplifiedRequest simplifiedRequest, Long polynomialId) {
        String simplifiedExpression = simplifyPolynomial(simplifiedRequest.getPolynomial());

        savePolynomial(simplifiedRequest, polynomialId, simplifiedExpression);
        return simplifiedExpression;
    }

    private String simplifyPolynomial(String polynomial) {
        try {
            ExprEvaluator evaluator = new ExprEvaluator();
            return evaluator.eval("Simplify(" + polynomial + ")").toString();
        } catch (SyntaxError e) {
            log.error("Error simplifying polynomial: {}", polynomial, e);
            throw e;
        }
    }

    private void savePolynomial(SimplifiedRequest simplifiedRequest, Long polynomialId,
                                String simplifiedExpression) {
        PolynomialCache polynomialCache = PolynomialCache.builder()
                .id(polynomialId)
                .inputExpression(simplifiedRequest.getPolynomial())
                .simplifiedExpression(simplifiedExpression)
                .createdAt(LocalDateTime.now())
                .build();

        polynomialRepository.save(polynomialCache);
        log.info("Polynomial saved in database");
    }

    @Override
    public Integer evaluate(EvaluateRequest evaluateRequest) {
        Long evaluatedId = generateNumericHash(evaluateRequest.getPolynomial());

        Optional<EvaluationCache> evaluationCache = evaluationRepository.findByIdAndInputValue(evaluatedId, evaluateRequest.getX());

        if (evaluationCache.isPresent()) {
            log.info("Evaluation was found in cache.");
            return evaluationCache.get().getOutputValue();
        }

        log.info("Evaluation not found in cache. Calculating...");
        return processCachedEvaluation(evaluateRequest, evaluatedId);
    }

    private Integer processCachedEvaluation(EvaluateRequest evaluateRequest, Long evaluatedId) {
        Integer calculatedValue = calculatePolynomialWithValue(evaluateRequest).intValue();
        saveEvaluation(evaluateRequest, evaluatedId, calculatedValue);
        return calculatedValue;
    }

    private Number calculatePolynomialWithValue(EvaluateRequest evaluateRequest) {
        String expression = evaluateRequest.getPolynomial().replace("x", String.valueOf(evaluateRequest.getX()));
        try {
            ExprEvaluator evaluator = new ExprEvaluator();
            return evaluator.eval(expression).toNumber();
        } catch (SyntaxError e) {
            log.error("Error calculating polynomial {} with value: {}", evaluateRequest.getPolynomial(), evaluateRequest.getX(), e);
            throw e;
        }
    }

    private void saveEvaluation(EvaluateRequest evaluateRequest, Long evaluatedId, Integer calculatedValue) {
        EvaluationCache evaluationCache = EvaluationCache.builder()
                .id(evaluatedId)
                .inputValue(evaluateRequest.getX())
                .outputValue(calculatedValue)
                .createdAt(LocalDateTime.now())
                .build();
        evaluationRepository.save(evaluationCache);
        log.info("Evaluation saved in database");
    }
}
