package com.github.muffindud.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({ "system:env", "system:properties", "classpath:discount.properties" })
public interface Discount extends Config {
    @Key("discount.price")
    float discountPrice();

    @Key("discount.rate")
    float discountRate();
}
