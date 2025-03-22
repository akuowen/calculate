package org.example.demo.newnode;

import org.example.demo.ex.CalculationException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FunctionNode implements ExpressionNode {
    private final String name;
    private final List<ExpressionNode> args;
    private final Function<List<Double>, Double> function;

    public FunctionNode(String name, List<ExpressionNode> args) {
        this.name = name;
        this.args = args;
        this.function = FunctionRegistry.getFunction(name);
    }

    @Override
    public double evaluate(Map<String, Double> context) throws CalculationException {
        try {
            List<Double> evaluatedArgs = args.stream()
                    .map(node -> node.evaluate(context))
                    .collect(Collectors.toList());
            return function.apply(evaluatedArgs);
        } catch (Exception e) {
            throw new CalculationException("函数计算错误: " + name + " - " + e.getMessage());
        }
    }

    @Override
    public Set<String> getVariables() {
        return args.stream()
                .flatMap(node -> node.getVariables().stream())
                .collect(Collectors.toSet());
    }
}
