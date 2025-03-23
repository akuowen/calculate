package io.ouka.demo;

import io.ouka.demo.ex.CalculationException;
import io.ouka.demo.ex.ParseException;
import io.ouka.demo.graph.DependencyGraph;
import io.ouka.demo.newnode.ExpressionNode;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CalculationEngine implements Observer {
    private final DependencyGraph dependencies;
    private final MetricValueManager valueManager;
    private final Map<String, ExpressionNode> expressions = new ConcurrentHashMap<>();

    public CalculationEngine(DependencyGraph dependencies, MetricValueManager valueManager) {
        this.dependencies = dependencies;
        this.valueManager = valueManager;
        valueManager.registerObserver(this, EventType.VALUE_UPDATED);
    }

    public void addExpression(String metric, String expression) throws CalculationException {
        try {
            ExpressionNode ast = new ExpressionParser(expression).parse();
            Set<String> newDeps = ast.getVariables();
            dependencies.updateDependency(metric, newDeps);
            expressions.put(metric, ast);
            calculate(metric);
        } catch (ParseException e) {
            throw new CalculationException("解析失败: " + metric + " - " + e.getMessage(), e);
        }
    }

    @Override
    public void update(EventType eventType, String metric, Object data) {
        if (eventType == EventType.VALUE_UPDATED) {
            dependencies.getDependents(metric).forEach(this::calculate);
        }
    }

    private void calculate(String metric) {
        Set<String> required = dependencies.getDependencies(metric);
        if (required.stream().allMatch(valueManager::containsMetric)) {
            try {
                Map<String, Double> context = required.stream()
                        .collect(Collectors.toMap(k -> k, valueManager::getValue));
                double newValue = expressions.get(metric).evaluate(context);
                Double oldValue = valueManager.getValue(metric);
                if (oldValue == null || Math.abs(newValue - oldValue) > 1e-6) {
                    valueManager.setValue(metric, newValue);
                    valueManager.notifyObservers(EventType.CALCULATION_DONE, metric, newValue);
                }
            } catch (CalculationException e) {
                System.err.println("计算错误: " + e.getMessage());
            }
        }
    }
}