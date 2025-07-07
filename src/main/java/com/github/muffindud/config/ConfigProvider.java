package com.github.muffindud.config;

import org.aeonbits.owner.ConfigCache;

public class ConfigProvider {
    public static Discount getDiscount() {
        return ConfigCache.getOrCreate(Discount.class);
    }
}
