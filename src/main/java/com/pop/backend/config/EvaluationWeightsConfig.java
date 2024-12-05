package com.pop.backend.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@ConfigurationProperties(prefix = "evaluation.weights")
public class EvaluationWeightsConfig {

    private static final Logger logger = LoggerFactory.getLogger(EvaluationWeightsConfig.class);

    private Map<String, Double> weights = new HashMap<>();

    public Map<String, Double> getWeights() {
        return weights;
    }

    public void setWeights(Map<String, Double> weights) {
        this.weights = weights;
    }

    @PostConstruct
    public void logWeights() {
        logger.info("Loaded evaluation weights: {}", weights);
    }

}