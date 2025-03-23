package io.ouka.demo.extend;

import io.ouka.demo.newnode.*;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class CodeGenerator {
    private final MethodVisitor mv;

    public CodeGenerator(MethodVisitor mv) {
        this.mv = mv;
    }

    public void generate(ExpressionNode node) {
        if (node instanceof ConstantNode) {
            generateConstant((ConstantNode) node);
        } else if (node instanceof VariableNode) {
            generateVariable((VariableNode) node);
        } else if (node instanceof BinaryOpNode) {
            generateBinaryOp((BinaryOpNode) node);
        } else if (node instanceof FunctionNode) {
            generateFunctionCall((FunctionNode) node);
        }
    }

    private void generateConstant(ConstantNode node) {
        mv.visitLdcInsn(node.getValue());
    }

    private void generateVariable(VariableNode node) {
        String varName = node.getName();
        mv.visitVarInsn(ALOAD, 1); // 加载 Map 参数
        mv.visitLdcInsn(varName);
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
            case "-":
                mv.visitInsn(DSUB);
                break;
            case "*":
                mv.visitInsn(DMUL);
                break;
            case "/":
                mv.visitInsn(DDIV);
                break;
        }
    }

    private void generateFunctionCall(FunctionNode node) {
        List<?> args = node.getArgs();
        int argCount = args.size();
        // 创建 double 数组
        mv.visitLdcInsn(argCount);
        mv.visitIntInsn(NEWARRAY, T_DOUBLE);
        for (int i = 0; i < argCount; i++) {
            mv.visitInsn(DUP);
            mv.visitLdcInsn(i);
            generate(node.getArgs().get(i));
            mv.visitInsn(DASTORE);
        }
        // 压入函数名
        mv.visitLdcInsn(node.getName());
        // 插入 SWAP 指令，交换栈顶两个元素，使得参数顺序为 (String, [D)
        mv.visitInsn(SWAP);
        // 调用辅助方法 FunctionUtils.callFunction(String funcName, double[] args)
        mv.visitMethodInsn(INVOKESTATIC, "io/ouka/demo/utils/FunctionUtils", "callFunction",
                "(Ljava/lang/String;[D)D", false);
    }
}