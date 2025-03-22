package org.example.demo;

import org.example.demo.graph.DependencyGraph;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MetricValueManager implements Subject {
    private final Map<String, Double> values = new ConcurrentHashMap<>();
    private final Map<EventType, Set<Observer>> observers = new ConcurrentHashMap<>();
    private final DependencyGraph dependencies;

    public MetricValueManager(DependencyGraph dependencies) {
        this.dependencies = dependencies;
    }

    public void setValue(String metric, double value) {
        Double previous = values.get(metric);
        // 精确比较浮点数变化
        if (previous == null || Math.abs(previous - value) > 1e-6) {
            values.put(metric, value);
            notifyObservers(EventType.VALUE_UPDATED, metric, value);
        }
    }

    public Double getValue(String metric) {
        return values.get(metric);
    }

    public boolean containsMetric(String metric) {
        return values.containsKey(metric);
    }

    public Map<String, Double> getAllValues() {
        return new HashMap<>(values);
    }

    @Override
    public void registerObserver(Observer observer, EventType eventType) {
        observers.computeIfAbsent(eventType, k -> ConcurrentHashMap.newKeySet()).add(observer);
    }

    @Override
    public void removeObserver(Observer observer, EventType eventType) {

    }

    @Override
    public void notifyObservers(EventType eventType, String metric, Object data) {
        observers.getOrDefault(eventType, Collections.emptySet())
                .forEach(observer -> observer.update(eventType, metric, data));
    }
}