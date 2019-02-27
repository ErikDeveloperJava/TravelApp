package net.travel.util.search;

import lombok.Getter;

public enum SearchModelType {

    HOTEL("hotel"),PLACE("place");

    @Getter
    private String name;

    SearchModelType(String name) {
        this.name = name;
    }
}
