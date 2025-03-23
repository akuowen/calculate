package io.ouka.demo.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DependencyGraph {
    private final Map<String, Set<String>> dependents = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> dependencies = new ConcurrentHashMap<>();

    public void addDependency(String metric, Set<String> dependsOn) {
        dependencies.put(metric, new HashSet<>(dependsOn));
        dependsOn.forEach(dep ->
                dependents.computeIfAbsent(dep, k -> ConcurrentHashMap.newKeySet()).add(metric)
        );
    }

    public Set<String> getDependents(String metric) {
        return Collections.unmodifiableSet(dependents.getOrDefault(metric, Collections.emptySet()));
    }

    public Set<String> getDependencies(String metric) {
        return Collections.unmodifiableSet(dependencies.getOrDefault(metric, Collections.emptySet()));
    }

    public void updateDependency(String metric, Set<String> newDependencies) {
        Set<String> oldDeps = dependencies.getOrDefault(metric, Collections.emptySet());
        oldDeps.forEach(dep -> {
            Set<String> deps = dependents.get(dep);
            if (deps != null) {
                deps.remove(metric);
            }
        });
        dependencies.put(metric, new HashSet<>(newDependencies));
        newDependencies.forEach(dep ->
                dependents.computeIfAbsent(dep, k -> ConcurrentHashMap.newKeySet()).add(metric)
        );
    }
}