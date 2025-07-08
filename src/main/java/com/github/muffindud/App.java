package com.github.muffindud;

import com.github.muffindud.service.BaseService;
import com.github.muffindud.service.impl.PizzaLand;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static final BaseService service = new PizzaLand();

    public static void main( String[] args ) {
        service.run();
    }
}
