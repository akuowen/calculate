package org.example.demo.newnode;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ConstantNode implements ExpressionNode {
    private final double value;

    public ConstantNode(double value) { this.value = value; }

    @Override
    public double evaluate(Map<String, Double> context) {
        return value;
    }

    @Override
    public Set<String> getVariables() {
        return Collections.emptySet();
    }
}