# BigDecimal AST 表达式引擎

[![License](http://img.shields.io/:license-Apache%202-blue.svg)](https://github.com/apache/arrow/blob/main/LICENSE.txt)

**Read this in other languages: [English](README.md)**

Calculate 是一个开源 Maven 项目，它使用抽象语法树 （AST）
解析和评估算术表达式并生成优化的字节码。目前设计为使用 'double' 进行计算，后续版本重构为使用 'BigDecimal'
来确保高精度运算，使其特别适用于金融和科学计算。

> **注意：**
> 该项目正在进行中。当前版本支持基于 BigDecimal 的计算的解释模式。未来的改进将包括扩展编译模式（字节码生成）以支持
> BigDecimal 作和进一步的性能优化。

## 功能

- **基于 AST 的解析和评估**
  将算术表达式（支持加、减、乘、除和基本函数调用）解析为 AST，并使用 'BigDecimal' 对其进行计算以保持高精度。

- **功能支持**
  通过中央 'FunctionRegistry' 对 'sqrt'、'pow'、'max'、'min' 和 'nvl' 等基本函数的内置支持。可以轻松注册自定义功能。

- **可变分辨率**
  变量从提供的上下文 （'Map<String， BigDecimal>“） 解析。如果未定义变量，则默认为零。

- **可扩展架构**
  AST 模块是可插拔的，可以在解释和（将来）编译模式之间进行选择。

- **未来编译模式（计划中）**
  尽管当前版本仅支持解释模式，但未来的版本将实现具有完全 BigDecimal 支持的编译模式，以实现更高的性能。

## 安装

克隆存储库并使用 Maven 构建项目：

```bash
git clone https://github.com/akuowen/calculate
cd calculate
mvn clean install
```

## 用法

下面是一个演示如何使用引擎计算算术表达式的示例：

```java
package io.ouka.demo;

import io.ouka.demo.ex.CalculationException;
import io.ouka.demo.graph.DependencyGraph;

public class DAGMetricCalculationExample {
    public static void main(String[] args) {
        DependencyGraph dependencyGraph = new DependencyGraph();
        MetricValueManager valueManager = new MetricValueManager(dependencyGraph);
        CalculationEngine engine = new CalculationEngine(dependencyGraph, valueManager);
        HistoryRecorder history = new HistoryRecorder(valueManager);

        try {
            engine.addExpression("profit", "revenue - cost");
            engine.addExpression("margin", "(profit / revenue) * 100");
            engine.addExpression("test", "profit * margin");

            valueManager.setValue("revenue", 150000.0);
            valueManager.setValue("cost", 150000.0);

            System.out.println("利润: " + valueManager.getValue("profit"));
            System.out.println("利润率: " + valueManager.getValue("margin") + "%");
            System.out.println("test: " + valueManager.getValue("test"));


            valueManager.setValue("revenue", 200000.0);

            System.out.println("利润: " + valueManager.getValue("profit"));
            System.out.println("利润率: " + valueManager.getValue("margin") + "%");
            System.out.println("test: " + valueManager.getValue("test"));


            history.getHistory().forEach(System.out::println);

        } catch (CalculationException e) {
            System.err.println("系统错误: " + e.getMessage());
        }
    }
}


```



## 未来的优化和改进

我们计划在未来的版本中实现以下增强功能：

### 一. 支持 BigDecimal 的编译模式

- **扩展 BigDecimal 操作的 AST 编译**
修改现有的抽象语法树（AST）编译流程，全面支持`BigDecimal`操作，从解释过渡到编译，提高性能。​:contentReference[oaicite:0]
{index=0}

### 二、性能优化

1. **增量计算机制**
   当输入稍有变化时，通过重复使用以前计算的结果来实施有效更新计算的策略，从而减少处理时间。

2. **缓存策略**
   开发和集成缓存机制来存储和重用昂贵的计算结果，最大限度地减少冗余处理并提高整体性能。

3. **动态方法调度优化**
   探索使用“invokedynamic”来优化动态方法调度，从而潜在地提高执行速度。

4. **AST 处理管道优化**
   优化 AST 处理管道，减少开销，提高效率。

### 三. 扩展性增强

1. **基于插件的功能系统**
   设计功能体系的插件化架构，让用户在不改变核心引擎的情况下，可以增加或修改功能，方便定制和扩展。

2. **DSL 扩展支持**
   引入对领域特定语言 (DSL) 的支持，使用户能够定义和解析针对特定问题领域的自定义表达式，从而增强灵活性和可用性。

3. **支持附加算术运算符和高级数学函数**
   扩大支持的运算范围，包括更多算术运算符和高级数学函数。

4. **用户定义函数**
   允许用户定义自定义函数并在`FunctionRegistry`中动态注册，增强引擎的灵活性。

### 四、稳定性和可靠性

1. **计算资源隔离**
   实施隔离计算资源的机制，确保各个计算不会互相干扰，从而增强系统稳定性。

2. **交易更新**
   结合事务处理以确保计算更新是原子的、一致的、隔离的和持久的（ACID），从而维护数据完整性。

### 五. 可观察性的改进

1. **计算过程追踪**
   开发工具来跟踪和监控计算过程，提供对执行流程的洞察并促进调试和优化。

2. **性能指标收集**
   实施系统来收集和分析性能指标，实现持续监控和识别潜在的瓶颈。

3. **增强错误处理**
   通过提供更详细、更用户友好的解析错误消息来改进错误处理。

### 六、高级功能扩展

1. **版本差异分析**
引入分析不同版本计算或表达式之间差异的能力，有助于跟踪变化并了解其影响。

2. **预测计算功能**
   开发允许引擎根据模式预测和预先计算潜在未来计算的功能，从而减少预期任务的延迟。

### 七. 分布式集群计算

1. **动态类加载机制**
   实现动态类加载系统，允许引擎在运行时加载和执行类，增强分布式环境中的灵活性和适应性。

2. **编译模式下的版本控制设计**
   为编译模式设计一个强大的版本控制系统来管理和跟踪编译表达式或函数的不同版本，确保跨分布式系统的一致性。

3. **网络通信优化**
   优化网络通信协议，增强数据传输效率，减少分布式计算场景下的延迟。

### 八. 配置增强

1. **精度和舍入模式配置**
   添加用于指定精度和舍入模式的运行时配置选项，允许用户根据特定要求定制计算。

2. **与日志和调试框架集成**
   改善与现有日志和调试框架的集成，以方便监控和故障排除。

### 九. 模块化

- **项目模块化**
  :contentReference[oaicite:1]{index=1}​:contentReference[oaicite:2]{index=2}

### 十. 其他数据类型和操作

- **支持混合类型操作**
  :contentReference[oaicite:3]{index=3}​:contentReference[oaicite:4]{index=4}

这些增强功能旨在提高 BigDecimal AST 表达引擎的性能、可扩展性、稳定性、可观察性和可伸缩性，确保它满足不断变化的用户需求和技术进步。

## 贡献

欢迎贡献！请复刻仓库、提交拉取请求或打开任何错误报告或功能请求的议题。对于重大更改，请先打开一个 issue 来讨论您的想法。

## 许可证

本项目根据 MIT 许可证获得许可。有关更多详细信息，请参阅 LICENSE 文件。
