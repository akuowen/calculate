package org.example.demo;

import org.example.demo.ex.CalculationException;
import org.example.demo.graph.DependencyGraph;

public class DAGMetricCalculationExample {
    public static void main(String[] args) {
        DependencyGraph dependencyGraph = new DependencyGraph();
        MetricValueManager valueManager = new MetricValueManager(dependencyGraph);
        CalculationEngine engine = new CalculationEngine(dependencyGraph, valueManager);
        HistoryRecorder history = new HistoryRecorder(valueManager);

        try {
            engine.addExpression("profit", "revenue - cost");
            engine.addExpression("margin", "(profit / revenue) * 100");

            valueManager.setValue("revenue", 150000.0);
            valueManager.setValue("cost", 150000.0);

            System.out.println("利润: " + valueManager.getValue("profit"));
            System.out.println("利润率: " + valueManager.getValue("margin") + "%");


            valueManager.setValue("revenue", 200000.0);

            System.out.println("利润: " + valueManager.getValue("profit"));
            System.out.println("利润率: " + valueManager.getValue("margin") + "%");


            history.getHistory().forEach(System.out::println);

        } catch (CalculationException e) {
            System.err.println("系统错误: " + e.getMessage());
        }
    }
}