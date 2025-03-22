package org.example.demo.newnode;

import org.example.demo.ex.CalculationException;

import java.util.Map;
import java.util.Set;

public interface ExpressionNode {
    double evaluate(Map<String, Double> context) throws CalculationException;
    Set<String> getVariables();
}