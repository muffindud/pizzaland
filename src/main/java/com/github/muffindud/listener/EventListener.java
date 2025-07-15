package com.github.muffindud.listener;

import com.github.muffindud.enums.NotificationTopic;

public interface EventListener {
    /**
     * Execute the function upon notification trigger
     *
     * @param topic for notification trigger
     * @param message to pass
     */
    void update(NotificationTopic topic, Object message);
}
