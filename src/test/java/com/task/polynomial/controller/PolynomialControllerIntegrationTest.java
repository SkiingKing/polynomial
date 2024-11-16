package com.task.polynomial.controller;


import com.task.polynomial.model.EvaluationCache;
import com.task.polynomial.model.PolynomialCache;
import com.task.polynomial.repository.EvaluationRepository;
import com.task.polynomial.repository.PolynomialRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.zip.CRC32;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PolynomialControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PolynomialRepository polynomialRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Test
    public void testSimplifyPolynomialWithoutCaching() throws Exception {
        String inputPolynomial = "2*x^2+3*x-5+x^2+x";
        String request = """
                {
                    "polynomial": "%s"
                }
                """.formatted(inputPolynomial);
        String simplifiedExpression = "-5+4*x+3*x^2";

        MvcResult result = mockMvc.perform(post("/v1/polynomial/simplify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertEquals(simplifiedExpression, response);
        Optional<PolynomialCache> polynomialCache = polynomialRepository.findById(generateNumericHash(inputPolynomial));
        assertTrue(polynomialCache.isPresent());
        assertEquals(simplifiedExpression, polynomialCache.get().getSimplifiedExpression());
    }

    @Test
    public void testSimplifyPolynomialWithCaching() throws Exception {
        String inputPolynomial = "x^2-2+2*x";
        PolynomialCache polynomialCache = PolynomialCache.builder()
                .id(generateNumericHash(inputPolynomial))
                .inputExpression(inputPolynomial)
                .simplifiedExpression("-2+2*x+x^2")
                .createdAt(LocalDateTime.now())
                .build();

        polynomialRepository.save(polynomialCache);

        String request = """
                {
                    "polynomial": "%s"
                }
                """.formatted(inputPolynomial);

        MvcResult result = mockMvc.perform(post("/v1/polynomial/simplify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertEquals("-2+2*x+x^2", response);

        long polynomialCount = polynomialRepository.count();
        assertEquals(1, polynomialCount);
    }


    @Test
    public void testSimplifyPolynomialWithNotValidPolynomialInRequest() throws Exception {
        String request = """
                {
                    "polynomial": "2*x^^2+3*x-5+x^^2+x"
                }
                """;

        MvcResult result = mockMvc.perform(post("/v1/polynomial/simplify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertThat(response).contains("Field 'polynomial': Wrong format for polynomial");

        long polynomialCount = polynomialRepository.count();
        assertEquals(0, polynomialCount);
    }

    @Test
    public void testEvaluationWithoutCaching() throws Exception {
        String inputPolynomial = "2*x^2+3*x-5+x^2+x";
        String request = """
                {
                    "polynomial": "%s",
                    "x": 5
                }
                """.formatted(inputPolynomial);

        MvcResult result = mockMvc.perform(post("/v1/polynomial/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertEquals("90", response);
        Optional<EvaluationCache> evaluationCache = evaluationRepository.findById(generateNumericHash(inputPolynomial));
        assertTrue(evaluationCache.isPresent());
        assertEquals(90, evaluationCache.get().getOutputValue());
    }

    @Test
    public void testEvaluationWithCaching() throws Exception {
        String inputPolynomial = "x^2-2+2*x";
        Integer inputXValue = 5;
        EvaluationCache evaluationCache = EvaluationCache.builder()
                .id(generateNumericHash(inputPolynomial))
                .inputValue(inputXValue)
                .outputValue(33)
                .createdAt(LocalDateTime.now())
                .build();

        evaluationRepository.save(evaluationCache);

        String request = """
                {
                    "polynomial": "%s",
                    "x": 5
                }
                """.formatted(inputPolynomial);

        MvcResult result = mockMvc.perform(post("/v1/polynomial/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertEquals("33", response);

        long evaluationCount = evaluationRepository.count();
        assertEquals(1, evaluationCount);
    }

    @Test
    public void testSimplifyPolynomialWithNotValidXValueInRequest() throws Exception {
        String request = """
                {
                    "polynomial": "2*x^2+3*x-5+x^2+x",
                    "x": 2b
                }
                """;

        mockMvc.perform(post("/v1/polynomial/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    private static long generateNumericHash(String inputExpression) {
        CRC32 crc32 = new CRC32();
        crc32.update(inputExpression.getBytes(StandardCharsets.UTF_8));
        return crc32.getValue();
    }
}
