package com.github.muffindud.view;

import com.github.muffindud.enums.Country;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CountryView {
    public static String getCountryMenuView() {
        return  "Select country:\n"
                + IntStream.range(1, Country.values().length + 1)
                .mapToObj(i -> {
                    Country country = Country.values()[i - 1];
                    return "[" + i + "]. " + country.name() + "\n";
                })
                .collect(Collectors.joining())
                + "\n[0]. Back\n";
    }
}
