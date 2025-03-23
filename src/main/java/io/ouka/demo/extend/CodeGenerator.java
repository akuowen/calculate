package io.ouka.demo.extend;

import io.ouka.demo.newnode.BinaryOpNode;
import io.ouka.demo.newnode.ExpressionNode;
import io.ouka.demo.newnode.VariableNode;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class CodeGenerator {
    private final MethodVisitor mv;

    public CodeGenerator(MethodVisitor mv) {
        this.mv = mv;
    }

    public void generate(ExpressionNode node) {
        if (node instanceof VariableNode) {
            generateVariable((VariableNode) node);
        } else if (node instanceof BinaryOpNode) {
            generateBinaryOp((BinaryOpNode) node);
        } // 其他节点类型处理...
    }

    private void generateVariable(VariableNode node) {
        String varName = node.getName();
        mv.visitVarInsn(ALOAD, 1); // 加载Map参数
        mv.visitLdcInsn(varName);   // 压入变量名
        mv.visitMethodInsn(INVOKEINTERFACE,
                "java/util/Map", "get",
                "(Ljava/lang/Object;)Ljava/lang/Object;", true);
        mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
        mv.visitMethodInsn(INVOKEVIRTUAL,
                "java/lang/Double", "doubleValue", "()D", false);
    }

    private void generateBinaryOp(BinaryOpNode node) {
        generate(node.getLeft());
        generate(node.getRight());

        switch (node.getOperator()) {
            case "+":
                mv.visitInsn(DADD);
                break;
            case "*":
                mv.visitInsn(DMUL);
                break;
            case "-":
                mv.visitInsn(DSUB);
                break;
            case "/":
                mv.visitInsn(DDIV);
                break;
        }
    }
}