package ru.otus;

import ru.otus.proxy.Ioc;

public class Main {

    public static void main(String[] args) {
        SomeTestService obj = Ioc.createClass(SomeTestServiceImpl.class);
        obj.calculation(2);
        obj.calculation(2, 3);
        obj.calculation(2, 3, "calculation");

        SomeTestService2 obj2 = Ioc.createClass(SomeTestService2Impl.class);
        obj2.calculation2(2);
        obj2.calculation2(2, 3);
        obj2.calculation2(22, 3, "calculation");
    }
}
