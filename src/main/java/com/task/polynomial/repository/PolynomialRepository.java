package com.task.polynomial.repository;

import com.task.polynomial.model.PolynomialCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolynomialRepository extends JpaRepository<PolynomialCache, Long> {
}
