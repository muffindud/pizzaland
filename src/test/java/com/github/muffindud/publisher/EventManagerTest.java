package com.github.muffindud.publisher;

import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.listener.EventListener;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EventManagerTest {
    private static final NotificationTopic topic1 = mock(NotificationTopic.class);
    private static final NotificationTopic topic2 = mock(NotificationTopic.class);

    @Test
    public void testUnsubscribeDoesntNotify() {
        EventManager manager = mock(EventManager.class);
        EventListener listener = mock(EventListener.class);

        manager.subscribe(listener, EventManagerTest.topic1);
        manager.unsubscribe(listener, EventManagerTest.topic1);
        manager.notifySubscribers(EventManagerTest.topic1, null);

        verify(listener, times(0)).update(EventManagerTest.topic1, null);
    }

    @Test
    public void testSubscribeNotifies() {
        EventManager manager = new EventManager();
        EventListener listener = mock(EventListener.class);

        doNothing().when(listener).update(isA(NotificationTopic.class), isA(Object.class));

        manager.subscribe(listener, EventManagerTest.topic1);
        manager.notifySubscribers(EventManagerTest.topic1, null);

        verify(listener, times(1)).update(EventManagerTest.topic1, null);
    }

    @Test
    public void testMultipleSubscribeNotifies() {
        EventManager manager = new EventManager();
        EventListener listener = mock(EventListener.class);

        manager.subscribe(listener, EventManagerTest.topic1);
        manager.subscribe(listener, EventManagerTest.topic2);

        manager.notifySubscribers(EventManagerTest.topic1, null);
        manager.notifySubscribers(EventManagerTest.topic2, null);

        verify(listener, times(1)).update(EventManagerTest.topic1, null);
        verify(listener, times(1)).update(EventManagerTest.topic2, null);
    }
}
