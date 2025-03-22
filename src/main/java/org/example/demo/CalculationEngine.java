package org.example.demo;

import org.example.demo.ex.CalculationException;
import org.example.demo.ex.ParseException;
import org.example.demo.graph.DependencyGraph;
import org.example.demo.newnode.ExpressionNode;

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

            // 更新依赖关系（关键修复）
            dependencies.updateDependency(metric, newDeps);
            expressions.put(metric, ast);

            // 立即计算初始值
            calculate(metric);

        } catch (ParseException e) {
            throw new CalculationException("解析失败: " + metric + " - " + e.getMessage());
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

                // 仅当值实际变化时更新
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