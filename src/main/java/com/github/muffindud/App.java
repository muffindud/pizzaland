package com.github.muffindud;

import com.github.muffindud.service.BaseService;
import com.github.muffindud.service.impl.PizzaLand;

public class App {
    public static final BaseService service = new PizzaLand();

    public static void main( String[] args ) {
        service.run();
    }
}
