package io.ouka.demo.extend;

import java.util.Map;

public class Test {
    public static void main(String[] args) {
        CompiledExpression compiled = ASTCompiler.compile("a+b");
        System.out.println("编译结果测试: " + compiled.execute(Map.of("a", 1.0, "b", 2.0)));
    }
}
