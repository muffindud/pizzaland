package com.github.muffindud.publisher;

import com.github.muffindud.listener.EventListener;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final List<EventListener> listeners = new ArrayList<>();

    public void subscribe(EventListener listener) {
        this.listeners.add(listener);
    }

    public void unsubscribe(EventListener listener) {
        this.listeners.remove(listener);
    }

    public void notifySubscribers() {
        for (EventListener listener : listeners) {
            listener.update();
        }
    }
}
