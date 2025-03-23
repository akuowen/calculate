package io.ouka.demo;

import java.util.*;

public class HistoryRecorder implements Observer {
    private final List<Map<String, Double>> history = new ArrayList<>();
    private final MetricValueManager valueManager;

    public List<Map<String, Double>> getHistory() {
        return history;
    }

    public HistoryRecorder(MetricValueManager valueManager) {
        this.valueManager = valueManager;
        valueManager.registerObserver(this, EventType.CALCULATION_DONE);
    }

    @Override
    public void update(EventType eventType, String metric, Object data) {
        if (eventType == EventType.CALCULATION_DONE) {
            // 去重检查：仅当与上次记录不同时保存
            Map<String, Double> current = valueManager.getAllValues();
            if (history.isEmpty() || !current.equals(history.get(history.size()-1))) {
                history.add(new HashMap<>(current));
            }
        }
    }

    public Map<String, Double> getSnapshot(int version) {
        return new HashMap<>(history.get(version));
    }

    @Override
    public String toString() {
        return "HistoryRecorder{" +
                "history=" + history +
                ", valueManager=" + valueManager +
                '}';
    }
}
