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

    private boolean isTopicPresent(NotificationTopic topic) {
        return this.listeners.containsKey(topic);
    }

    /**
     * Subscribe the listener to a specified topic
     *
     * @param listener to subscribe
     * @param topic to subscribe the listener to
     */
    public void subscribe(EventListener listener, NotificationTopic topic) {
        if (!this.isTopicPresent(topic)) {
            this.listeners.put(topic, new HashSet<>());
        }
        this.listeners.get(topic).add(listener);
    }

    /**
     * Unsubscribe the listener from a specified topic
     *
     * @param listener to unsubscribe
     * @param topic to unsubscribe the listener from
     */
    public void unsubscribe(EventListener listener, NotificationTopic topic) {
        this.listeners.get(topic).remove(listener);
    }

    /**
     * Notify all the listeners subscribed to the topic passing the message object
     *
     * @param topic to notify the subscribed listeners
     * @param message to pass along
     */
    public void notifySubscribers(NotificationTopic topic, Object message) {
        if (this.isTopicPresent(topic)) {
            for (EventListener listener : this.listeners.get(topic)) {
                listener.update(topic, message);
            }
        }
    }
}
