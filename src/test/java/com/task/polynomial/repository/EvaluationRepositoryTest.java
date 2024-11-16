package com.task.polynomial.repository;

import com.task.polynomial.model.EvaluationCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EvaluationRepositoryTest {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Test
    void testFindByIdAndInputValueSuccess() {
        EvaluationCache evaluationCache = EvaluationCache.builder()
                .id(1L)
                .inputValue(5)
                .outputValue(25)
                .createdAt(LocalDateTime.now())
                .build();
        evaluationRepository.save(evaluationCache);

        Optional<EvaluationCache> result = evaluationRepository.findByIdAndInputValue(1L, 5);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getInputValue()).isEqualTo(5);
        assertThat(result.get().getOutputValue()).isEqualTo(25);
    }

    @Test
    void testFindByIdAndInputValueUnsuccessful() {
        EvaluationCache evaluationCache = EvaluationCache.builder()
                .id(1L)
                .inputValue(5)
                .outputValue(25)
                .createdAt(LocalDateTime.now())
                .build();
        evaluationRepository.save(evaluationCache);

        Optional<EvaluationCache> result = evaluationRepository.findByIdAndInputValue(1L, 6);

        assertThat(result).isEmpty();
    }
}
