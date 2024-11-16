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
@Table(name = "evaluation_cache")
public class EvaluationCache {

    @Id
    private Long id;

    @Column(name = "input_value", nullable = false)
    private Integer inputValue;

    @Column(name = "calculated_value", nullable = false)
    private Integer outputValue;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
