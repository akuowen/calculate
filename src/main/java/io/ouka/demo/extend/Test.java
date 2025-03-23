package io.ouka.demo.extend;

public class Test {
    public static void main(String[] args) {
        CompiledExpression compiled = ASTCompiler.compile("a+b");
        System.out.println("test");
    }
}
