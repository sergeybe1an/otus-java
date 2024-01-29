package ru.otus;

import ru.otus.annotations.handlers.TestProcessing;

public class Main {

    public static void main(String[] args) {
        TestProcessing.process(SomeClassTest.class.getName());
    }
}
