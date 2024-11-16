package com.task.polynomial.service;

import com.task.polynomial.domain.EvaluateRequest;
import com.task.polynomial.domain.SimplifiedRequest;
import com.task.polynomial.model.EvaluationCache;
import com.task.polynomial.model.PolynomialCache;
import com.task.polynomial.repository.EvaluationRepository;
import com.task.polynomial.repository.PolynomialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PolynomialServiceTest {

    @Mock
    private PolynomialRepository polynomialRepository;

    @Mock
    private EvaluationRepository evaluationRepository;

    @Mock
    private ExprEvaluator evaluator;

    @Mock
    private IExpr iExpr;

    @InjectMocks
    private PolynomialService polynomialService;

    @Test
    void shouldSimplifyNotCachedPolynomialSuccessfully() {
        String simplifiedPolynomial = "-5+4*x+3*x^2";
        SimplifiedRequest simplifiedRequest = new SimplifiedRequest();
        simplifiedRequest.setPolynomial("2*x^2+3*x-5+x^2+x");

        when(polynomialRepository.findById(anyLong())).thenReturn(Optional.empty());

        String result = polynomialService.simplify(simplifiedRequest);

        verify(polynomialRepository, times(1)).save(any(PolynomialCache.class));
        assertEquals(simplifiedPolynomial, result);
    }

    @Test
    void shouldSimplifyPolynomialCachedInDBSuccessfully() {
        String simplifiedPolynomial = "-5+4*x+3*x^2";
        SimplifiedRequest simplifiedRequest = new SimplifiedRequest();
        simplifiedRequest.setPolynomial("2*x^2+3*x-5+x^2+x");

        when(polynomialRepository.findById(anyLong())).thenReturn(createPolynomialCache(simplifiedPolynomial));

        String result = polynomialService.simplify(simplifiedRequest);

        verify(polynomialRepository, times(1)).findById(anyLong());
        verify(polynomialRepository, never()).save(any(PolynomialCache.class));
        assertEquals(simplifiedPolynomial, result);
    }

    @Test
    void shouldEvaluatedNotCachedPolynomialSuccessfully() {
        EvaluateRequest evaluateRequest = new EvaluateRequest();
        evaluateRequest.setPolynomial("x^2+x+3");
        evaluateRequest.setX(2);

        when(evaluationRepository.findByIdAndInputValue(anyLong(), anyInt())).thenReturn(Optional.empty());

        Integer result = polynomialService.evaluate(evaluateRequest);

        verify(evaluationRepository, times(1)).save(any(EvaluationCache.class));
        assertEquals(9, result);
    }

    @Test
    void shouldEvaluatePolynomialCachedInDBSuccessfully() {
        Integer expectedOutputResult = 9;
        EvaluateRequest evaluateRequest = new EvaluateRequest();
        evaluateRequest.setPolynomial("x^2+x+3");
        evaluateRequest.setX(2);

        when(evaluationRepository.findByIdAndInputValue(anyLong(), anyInt())).thenReturn(createEvaluationCache(expectedOutputResult));

        Integer result = polynomialService.evaluate(evaluateRequest);

        verify(evaluationRepository, times(1)).findByIdAndInputValue(anyLong(), anyInt());
        verify(evaluationRepository, never()).save(any(EvaluationCache.class));
        assertEquals(expectedOutputResult, result);
    }

    private static Optional<PolynomialCache> createPolynomialCache(String simplifiedExpression) {
        return Optional.of(PolynomialCache.builder()
                .id(1L)
                .simplifiedExpression(simplifiedExpression)
                .build());

    }

    private static Optional<EvaluationCache> createEvaluationCache(Integer outputValue) {
        return Optional.of(EvaluationCache.builder()
                .id(1L)
                .outputValue(outputValue)
                .build());

    }
}
