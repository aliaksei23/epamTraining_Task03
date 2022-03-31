package com.company.task03.util;

public class TruckIdGenerator {

    private static int currentId = 0;

    public TruckIdGenerator() {
    }

    public static int generateId() {
        return currentId++;
    }

}
