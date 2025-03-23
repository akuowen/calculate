package io.ouka.demo.utils;

import io.ouka.demo.newnode.FunctionRegistry;

import java.util.ArrayList;
import java.util.List;

public class FunctionUtils {
    public static double callFunction(String funcName, double[] args) {
        List<Double> params = new ArrayList<>();
        for (double d : args) {
            params.add(d);
        }
        return FunctionRegistry.getFunction(funcName).apply(params);
    }
}