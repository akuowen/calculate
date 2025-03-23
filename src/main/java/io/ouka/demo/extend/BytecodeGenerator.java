package io.ouka.demo.extend;

import io.ouka.demo.ex.CalculationException;
import io.ouka.demo.newnode.ExpressionNode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static org.objectweb.asm.Opcodes.*;

public class BytecodeGenerator extends ClassLoader {
    private static final String CLASS_NAME_PREFIX = "CompiledExpr_";

    public CompiledExpression compile(ExpressionNode ast) {
        String className = CLASS_NAME_PREFIX + UUID.randomUUID().toString().replace("-", "");
        byte[] bytecode = generateBytecode(className, ast);
        Class<?> clazz = defineClass(className, bytecode, 0, bytecode.length);
        try {
            return (CompiledExpression) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new CalculationException("实例化失败", e);
        }
    }

    private byte[] generateBytecode(String className, ExpressionNode ast) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        // 类定义
        cw.visit(V1_8, ACC_PUBLIC, className.replace('.', '/'),
                null, "java/lang/Object",
//                src/main/java/io/ouka/demo/extend/CompiledExpression.java
                new String[]{"io/ouka/demo/extend/CompiledExpression"});

        // 生成构造函数
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        // 生成execute方法
        mv = cw.visitMethod(ACC_PUBLIC, "execute",
                "(Ljava/util/Map;)D",
                "(Ljava/util/Map<Ljava/lang/String;Ljava/lang/Double;>;)D", null);

        new CodeGenerator(mv).generate(ast);

        mv.visitInsn(DRETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        cw.visitEnd();

        byte[] bytes = cw.toByteArray();
        try {
            FileOutputStream fos = new FileOutputStream("Example.class");
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cw.toByteArray();
    }
}