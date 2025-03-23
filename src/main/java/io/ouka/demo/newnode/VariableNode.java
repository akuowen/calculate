package io.ouka.demo.newnode;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class VariableNode implements ExpressionNode {
    private final String name;

    public VariableNode(String name) { this.name = name; }

    @Override
    public double evaluate(Map<String, Double> context) {
        return context.getOrDefault(name, 0.0);
    }

    @Override
    public Set<String> getVariables() {
        return Collections.singleton(name);
    }
}