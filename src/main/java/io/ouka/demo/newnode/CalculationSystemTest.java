package io.ouka.demo.newnode;

import io.ouka.demo.ex.CalculationException;

import java.util.Map;

public class CalculationSystemTest {
    public static void main(String[] args) {
        testExpression("2+3*4", Map.of(), 14.0);
        testExpression("nvl(a,5)+sqrt(b)", Map.of("b", 9.0), 5.0 + 3.0);
        testExpression("pow(2,3)*(max(5,3)-min(4,2))", Map.of(), (Math.pow(2, 3) * (5 - 2)));
    }

    private static void testExpression(String expr, Map<String, Double> context, double expected) {
        try {
            ExpressionNode ast = new ExpressionParser(expr).parse();
            double result = ast.evaluate(context);
            System.out.printf("测试表达式: %-20s 结果: %.2f (预期: %.2f) %s%n",
                    expr,
                    result,
                    expected,
                    Math.abs(result - expected) < 1e-6 ? "✓" : "✗");
        } catch (CalculationException e) {
            System.err.println("表达式错误: " + expr + " - " + e.getMessage());
        }
    }
}