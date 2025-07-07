package com.github.muffindud.publisher;

import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.listener.EventListener;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class EventManager {
    private final ConcurrentMap<NotificationTopic, Set<EventListener>> listeners =
            new ConcurrentHashMap<>();

    public EventManager() {
        for (NotificationTopic topic : NotificationTopic.values()) {
            this.listeners.put(topic, new HashSet<>());
        }
    }

    public void subscribe(EventListener listener, NotificationTopic topic) {
        this.listeners.get(topic).add(listener);
    }

    public void unsubscribe(EventListener listener, NotificationTopic topic) {
        this.listeners.get(topic).remove(listener);
    }

    public void notifySubscribers(NotificationTopic topic) {
        for (EventListener listener : this.listeners.get(topic)) {
            listener.update(topic);
        }
    }
}
