package com.task.polynomial.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "polynomial_cache")
public class PolynomialCache {

    @Id
    private Long id;

    @Column(name = "input_expression", nullable = false)
    private String inputExpression;

    @Column(name = "simplified_expression", nullable = false)
    private String simplifiedExpression;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
