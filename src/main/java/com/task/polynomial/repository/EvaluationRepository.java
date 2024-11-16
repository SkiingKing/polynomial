package com.task.polynomial.repository;

import com.task.polynomial.model.EvaluationCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<EvaluationCache, Long> {

    Optional<EvaluationCache> findByIdAndInputValue(Long id, Integer inputValue);
}
