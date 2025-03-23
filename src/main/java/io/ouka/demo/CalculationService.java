package io.ouka.demo;

import io.ouka.demo.extend.ASTCompiler;
import io.ouka.demo.extend.CompiledExpression;
import io.ouka.demo.ex.CalculationException;
import io.ouka.demo.newnode.ExpressionNode;
import io.ouka.demo.newnode.ExpressionParser;
import io.ouka.demo.ex.ParseException;
import java.util.Map;

public class CalculationService {
    private final ExecutionMode mode;

    public CalculationService(ExecutionMode mode) {
        this.mode = mode;
    }

    public double evaluate(String expression, Map<String, Double> context) throws CalculationException {
        if (mode == ExecutionMode.INTERPRET) {
            try {
                ExpressionNode ast = new ExpressionParser(expression).parse();
                return ast.evaluate(context);
            } catch (ParseException e) {
                throw new CalculationException("解析失败: " + e.getMessage(), e);
            }
        } else { // COMPILE 模式
            CompiledExpression compiled = ASTCompiler.compile(expression);
            return compiled.execute(context);
        }
    }
}