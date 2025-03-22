package org.example.demo;

public enum EventType {
    EXPRESSION_PARSED,  // 表达式解析完成
    EXPRESSION_ADDED,   // 表达式添加完成
    VALUE_UPDATED,      // 值更新事件
    CALCULATION_DONE    // 计算完成事件
}
