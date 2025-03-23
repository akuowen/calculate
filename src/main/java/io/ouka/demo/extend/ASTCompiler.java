package io.ouka.demo.extend;

import io.ouka.demo.ex.CalculationException;
import io.ouka.demo.ex.ParseException;
import io.ouka.demo.newnode.ExpressionNode;
import io.ouka.demo.newnode.ExpressionParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ASTCompiler {
    private static final Map<String, CompiledExpression> CACHE = new ConcurrentHashMap<>();

    public static CompiledExpression compile(String expression) {
        return CACHE.computeIfAbsent(expression, expr -> {
            try {
                ExpressionNode ast = new ExpressionParser(expr).parse();

                return new BytecodeGenerator().compile(ast);
            } catch (ParseException e) {
                throw new CalculationException("编译失败: " + expr, e);
            }
        });
    }
}