# BigDecimal AST Expression Engine


[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Read this in other languages: [简体中文](README_CN.md)**

Calculate an open-source Maven project that parses and evaluates arithmetic expressions using an Abstract Syntax Tree (AST) and generates optimized bytecode. Originally designed to work with `double` for computations, this version has been refactored to use `BigDecimal` to ensure high-precision arithmetic, making it especially suitable for financial and scientific calculations.

> **Note:**  
> This project is a work in progress. The current version supports interpretation mode with BigDecimal-based evaluation. Future improvements will include extending the compile mode (bytecode generation) to support BigDecimal operations and further performance optimizations.

## Features

- **AST-Based Parsing & Evaluation**  
  Parses arithmetic expressions (supporting addition, subtraction, multiplication, division, and basic function calls) into an AST, and evaluates them using `BigDecimal` to maintain high precision.

- **Function Support**  
  Built-in support for basic functions such as `sqrt`, `pow`, `max`, `min`, and `nvl` through a central `FunctionRegistry`. Custom functions can be easily registered.

- **Variable Resolution**  
  Variables are resolved from a provided context (`Map<String, BigDecimal>`). If a variable is not defined, it defaults to zero.

- **Extensible Architecture**  
  The AST module is pluggable, enabling the choice between interpretation and (in future) compilation modes.

- **Future Compile Mode (Planned)**  
  Although the current release only supports interpretation mode, future releases will implement a compile mode with full BigDecimal support for even higher performance.

## Installation

Clone the repository and build the project using Maven:

```bash
git clone https://github.com/akuowen/calculate
cd calculate
mvn clean install
```

## Usage
Below is an example demonstrating how to evaluate arithmetic expressions using the engine:

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

## Future Optimizations & Improvements

We plan to implement the following enhancements in future releases:

### I. Compile Mode with BigDecimal Support

- **Extend AST Compilation for BigDecimal Operations**  
  Modify the existing Abstract Syntax Tree (AST) compilation process to fully support `BigDecimal` operations, transitioning from interpretation to compilation for improved performance.&#8203;:contentReference[oaicite:0]{index=0}

### II. Performance Optimization

1. **Incremental Computation Mechanism**  
   Implement strategies to update computations efficiently by reusing previously computed results when inputs change slightly, reducing processing time.

2. **Caching Strategies**  
   Develop and integrate caching mechanisms to store and reuse results of expensive computations, minimizing redundant processing and enhancing overall performance.

3. **Dynamic Method Dispatch Optimization**  
   Explore the use of `invokedynamic` to optimize dynamic method dispatch, potentially improving execution speed.

4. **AST Processing Pipeline Optimization**  
   Optimize the AST processing pipeline to reduce overhead and enhance efficiency.

### III. Extensibility Enhancements

1. **Plugin-Based Function System**  
   Design a plugin architecture for the function system, allowing users to add or modify functions without altering the core engine, facilitating customization and scalability.

2. **DSL Extension Support**  
   Introduce support for Domain-Specific Languages (DSLs) to enable users to define and parse custom expressions tailored to specific problem domains, enhancing flexibility and usability.

3. **Support for Additional Arithmetic Operators and Advanced Mathematical Functions**  
   Expand the range of supported operations to include more arithmetic operators and advanced mathematical functions.

4. **User-Defined Functions**  
   Allow users to define custom functions and register them dynamically within the `FunctionRegistry`, enhancing the engine's flexibility.

### IV. Stability and Reliability

1. **Isolation of Computational Resources**  
   Implement mechanisms to isolate computational resources, ensuring that individual calculations do not interfere with each other, thereby enhancing system stability.

2. **Transactional Updates**  
   Incorporate transactional processing to ensure that updates to computations are atomic, consistent, isolated, and durable (ACID), maintaining data integrity.

### V. Observability Improvements

1. **Computation Process Tracing**  
   Develop tools to trace and monitor the computation process, providing insights into the execution flow and facilitating debugging and optimization.

2. **Performance Metrics Collection**  
   Implement systems to collect and analyze performance metrics, enabling continuous monitoring and identification of potential bottlenecks.

3. **Enhanced Error Handling**  
   Improve error handling by providing more detailed and user-friendly parsing error messages.

### VI. Advanced Feature Expansion

1. **Version Difference Analysis**  
   Introduce capabilities to analyze differences between various versions of computations or expressions, aiding in tracking changes and understanding their impacts.

2. **Predictive Computation Functionality**  
   Develop features that allow the engine to predict and pre-compute potential future computations based on patterns, reducing latency for anticipated tasks.

### VII. Distributed Cluster Computing

1. **Dynamic Class Loading Mechanism**  
   Implement a dynamic class loading system to allow the engine to load and execute classes at runtime, enhancing flexibility and adaptability in a distributed environment.

2. **Version Control Design in Compile Mode**  
   Design a robust version control system for the compile mode to manage and track different versions of compiled expressions or functions, ensuring consistency across distributed systems.

3. **Network Communication Optimization**  
   Optimize network communication protocols to enhance data transfer efficiency and reduce latency in distributed computing scenarios.

### VIII. Configuration Enhancements

1. **Precision and Rounding Mode Configuration**  
   Add runtime configuration options for specifying precision and rounding modes, allowing users to tailor calculations to their specific requirements.

2. **Integration with Logging and Debugging Frameworks**  
   Improve integration with existing logging and debugging frameworks to facilitate monitoring and troubleshooting.

### IX. Modularization

- **Project Modularization**  
  :contentReference[oaicite:1]{index=1}&#8203;:contentReference[oaicite:2]{index=2}

### X. Additional Data Types and Operations

- **Support for Mixed-Type Operations**  
  :contentReference[oaicite:3]{index=3}&#8203;:contentReference[oaicite:4]{index=4}

These enhancements aim to improve the performance, extensibility, stability, observability, and scalability of the BigDecimal AST Expression Engine, ensuring it meets evolving user needs and technological advancements.


## Contributing

Contributions are welcome! Please fork the repository, submit pull requests, or open issues for any bug reports or feature requests. For major changes, please open an issue first to discuss your ideas.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.
