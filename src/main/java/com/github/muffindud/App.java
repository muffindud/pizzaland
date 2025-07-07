package com.github.muffindud;

import com.github.muffindud.controller.MenuController;
import com.github.muffindud.enums.NotificationTopic;
import com.github.muffindud.publisher.EventManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App 
{
    public static final EventManager eventManager = new EventManager();
    public static final MenuController menuController = new MenuController();

    public static void main( String[] args ) {
        eventManager.subscribe(menuController, NotificationTopic.DISCOUNT_APPLIED);
        eventManager.subscribe(menuController, NotificationTopic.DISCOUNT_NOT_APPLIED);

        menuController.run();
    }
}
