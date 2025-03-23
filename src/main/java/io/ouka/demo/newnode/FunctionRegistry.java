package io.ouka.demo.newnode;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class FunctionRegistry {
    private static final Map<String, Function<List<Double>, Double>> functions = new ConcurrentHashMap<>();

    static {
        register("sqrt", params -> Math.sqrt(params.get(0)));
        register("nvl", params -> params.get(0) != null ? params.get(0) : params.get(1));
        register("max", params -> params.stream().max(Double::compare).orElse(0.0));
        register("min", params -> params.stream().min(Double::compare).orElse(0.0));
        register("pow", params -> Math.pow(params.get(0), params.get(1)));
    }

    public static void register(String name, Function<List<Double>, Double> function) {
        functions.put(name.toLowerCase(), function);
    }

    public static Function<List<Double>, Double> getFunction(String name) {
        Function<List<Double>, Double> func = functions.get(name.toLowerCase());
        if (func == null) throw new IllegalArgumentException("未知函数: " + name);
        return func;
    }
}