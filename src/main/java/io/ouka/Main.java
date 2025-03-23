package io.ouka;

import io.ouka.demo.CalculationService;
import io.ouka.demo.ExecutionMode;
import io.ouka.demo.ex.CalculationException;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        CalculationService interpreterService = new CalculationService(ExecutionMode.INTERPRET);
        CalculationService compilerService = new CalculationService(ExecutionMode.COMPILE);

        Map<String, Double> context = Map.of("a", 5.0, "b", 3.0, "x", 2.0);
        String expr1 = "a+b*2";
        String expr2 = "sqrt(a)+pow(b,x)";

        try {
            double result1Interp = interpreterService.evaluate(expr1, context);
            double result1Compile = compilerService.evaluate(expr1, context);
            double result2Interp = interpreterService.evaluate(expr2, context);
            double result2Compile = compilerService.evaluate(expr2, context);

            System.out.println("表达式: " + expr1);
            System.out.println("解释执行结果: " + result1Interp);
            System.out.println("编译执行结果: " + result1Compile);

            System.out.println("表达式: " + expr2);
            System.out.println("解释执行结果: " + result2Interp);
            System.out.println("编译执行结果: " + result2Compile);
        } catch (CalculationException e) {
            System.err.println("计算错误: " + e.getMessage());
        }
    }
}