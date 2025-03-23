package io.ouka.demo;

public interface Observer {
    void update(EventType eventType, String metric, Object data);
}