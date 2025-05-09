package io.ouka.demo.newnode;

import io.ouka.demo.ex.CalculationException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class BinaryOpNode implements ExpressionNode {
    private final ExpressionNode left;
    private final ExpressionNode right;
    private final BiFunction<Double, Double, Double> operation;
    private final String operator;

    public BinaryOpNode(ExpressionNode left, ExpressionNode right, String op) {
        this.left = left;
        this.right = right;
        this.operation = createOperation(op);
        this.operator = op;
    }

    private BiFunction<Double, Double, Double> createOperation(String op) {
        switch (op) {
            case "+":
                return (a, b) -> a + b;
            case "-":
                return (a, b) -> a - b;
            case "*":
                return (a, b) -> a * b;
            case "/":
                return (a, b) -> {
                    if (b == 0) throw new ArithmeticException("Division by zero");
                    return a / b;
                };
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }

    public String getOperator() {
        return operator;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public ExpressionNode getRight() {
        return right;
    }

    @Override
    public double evaluate(Map<String, Double> context) throws CalculationException {
        try {
            return operation.apply(left.evaluate(context), right.evaluate(context));
        } catch (Exception e) {
            throw new CalculationException("计算错误: " + e.getMessage(), e);
        }
    }

    @Override
    public Set<String> getVariables() {
        Set<String> vars = new HashSet<>(left.getVariables());
        vars.addAll(right.getVariables());
        return vars;
    }
}