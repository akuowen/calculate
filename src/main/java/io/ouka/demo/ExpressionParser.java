package io.ouka.demo;

import io.ouka.demo.ex.ParseException;
import io.ouka.demo.newnode.*;
import org.example.demo.newnode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ExpressionParser {
    private final String expression;
    private int pos = 0;
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?([eE][+-]?\\d+)?");

    public ExpressionParser(String expression) {
        this.expression = expression.replaceAll("\\s+", "") + "#"; // 添加结束符
    }

    public ExpressionNode parse() throws ParseException {
        ExpressionNode node = parseExpression();
        if (pos < expression.length() - 1) { // 忽略结束符#
            throw new ParseException("未预期的结束位置: " + pos);
        }
        return node;
    }

    private ExpressionNode parseExpression() throws ParseException {
        ExpressionNode left = parseTerm();
        while (pos < expression.length()) {
            char c = expression.charAt(pos);
            if (c == '+' || c == '-') {
                pos++;
                ExpressionNode right = parseTerm();
                left = new BinaryOpNode(left, right, String.valueOf(c));
            } else {
                break;
            }
        }
        return left;
    }

    private ExpressionNode parseTerm() throws ParseException {
        ExpressionNode left = parseFactor();
        while (pos < expression.length()) {
            char c = expression.charAt(pos);
            if (c == '*' || c == '/') {
                pos++;
                ExpressionNode right = parseFactor();
                left = new BinaryOpNode(left, right, String.valueOf(c));
            } else {
                break;
            }
        }
        return left;
    }

    private ExpressionNode parseFactor() throws ParseException {
        if (pos >= expression.length()) {
            throw new ParseException("意外结束的表达式");
        }
        char c = expression.charAt(pos);
        if (Character.isLetter(c)) {
            return parseFunctionOrVariable();
        } else if (Character.isDigit(c) || c == '.' || c == '-') {
            return parseNumber();
        } else if (c == '(') {
            pos++;
            ExpressionNode node = parseExpression();
            expect(')');
            return node;
        }
        throw new ParseException("无法解析的字符: '" + c + "' 位置: " + pos);
    }

    private ExpressionNode parseFunctionOrVariable() throws ParseException {
        StringBuilder sb = new StringBuilder();
        while (pos < expression.length() && (Character.isLetterOrDigit(expression.charAt(pos)) || expression.charAt(pos) == '_')) {
            sb.append(expression.charAt(pos++));
        }

        if (pos < expression.length() && expression.charAt(pos) == '(') {
            pos++;
            List<ExpressionNode> args = parseArguments();
            expect(')');
            return new FunctionNode(sb.toString(), args);
        } else {
            return new VariableNode(sb.toString());
        }
    }

    private List<ExpressionNode> parseArguments() throws ParseException {
        List<ExpressionNode> args = new ArrayList<>();
        while (true) {
            args.add(parseExpression());
            if (pos >= expression.length()) break;
            if (expression.charAt(pos) == ')') break;
            expect(',');
        }
        return args;
    }

    private ExpressionNode parseNumber() throws ParseException {
        java.util.regex.Matcher matcher = NUMBER_PATTERN.matcher(expression.substring(pos));
        if (matcher.find()) {
            String numStr = matcher.group();
            pos += numStr.length();
            try {
                return new ConstantNode(Double.parseDouble(numStr));
            } catch (NumberFormatException e) {
                throw new ParseException("无效数字格式: " + numStr);
            }
        }
        throw new ParseException("期望数字 位置: " + pos);
    }

    private void expect(char expected) throws ParseException {
        if (pos >= expression.length() || expression.charAt(pos++) != expected) {
            throw new ParseException("期望字符 '" + expected + "' 位置: " + (pos-1));
        }
    }
}