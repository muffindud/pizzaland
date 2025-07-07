package com.github.muffindud.listener;

import com.github.muffindud.enums.NotificationTopic;

public interface EventListener {
    void update(NotificationTopic topic);
}
