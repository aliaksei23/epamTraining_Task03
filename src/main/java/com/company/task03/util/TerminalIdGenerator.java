package com.company.task03.util;

public class TerminalIdGenerator {

    private static int currentId = 0;

    public TerminalIdGenerator() {
    }

    public static int generateId() {
        return currentId++;
    }
}
