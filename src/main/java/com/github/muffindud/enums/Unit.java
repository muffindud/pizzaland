package com.github.muffindud.enums;

import lombok.Getter;

public enum Unit {
    GRAM("g"),
    LITER("l");

    @Getter private final String unitName;

    Unit(String unit) {
        this.unitName = unit;
    }
}
