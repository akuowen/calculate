package io.ouka.demo.extend;

import java.util.Map;

public interface CompiledExpression {
    double execute(Map<String, Double> context);
}

