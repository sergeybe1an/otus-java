package ru.otus;

import ru.otus.annotations.Log;

public class SomeTestService2Impl implements SomeTestService2 {

    @Log
    public void calculation2(int param1) {
        System.out.println(param1 * param1);
    }

    @Log
    public void calculation2(int param1, int param2) {
        System.out.println(param1 * param1 + param2 * param2);
    }

    @Log
    public void calculation2(int param1, int param2, String param3) {
        System.out.println(param3 + " " + (param1 * param1 + param2 * param2));
    }
}
