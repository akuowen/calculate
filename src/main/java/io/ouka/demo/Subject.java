package io.ouka.demo;

public interface Subject {
    void registerObserver(Observer observer, EventType eventType);
    void removeObserver(Observer observer, EventType eventType);
    void notifyObservers(EventType eventType, String metric, Object data);
}