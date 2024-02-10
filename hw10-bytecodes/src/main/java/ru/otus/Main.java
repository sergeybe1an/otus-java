package ru.otus;

import ru.otus.proxy.Ioc;

public class Main {

    public static void main(String[] args) {
        SomeTestService obj = Ioc.createClass();
        obj.calculation(2);
        obj.calculation(2, 3);
        obj.calculation(2, 3, "calculation");
    }
}
