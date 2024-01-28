package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.annotations.handlers.AssertionException;

public class SomeClassTest {

    private int expected = 0;
    private StringBuilder expectedSb = new StringBuilder();

    @Before
    public void beforeFirst() {
        expected += 1;
        expectedSb.append("beforeOne");
    }

    @Before
    public void beforeSecond() {
        expected += 2;
        expectedSb.append("beforeTwo");
    }

    @After
    public void afterFirst() {
        expected = 0;
    }

    @After
    public void afterSecond() {
        expectedSb.setLength(0);
    }

    @Test
    public void testOne() {
        int actual = 4;
        expected += 1;
        if (expected != actual) {
            throw new AssertionException(expected + " != " + actual);
        }
    }

    @Test
    public void testTwo() {
        String actual = "beforeOnebeforeTwo!";
        expectedSb.append("!");
        if (!expectedSb.toString().equals(actual)) {
            throw new AssertionException(expectedSb + " not equals " + actual);
        }
    }

    @Test
    public void testThree() {
        String actual = "beforeOnebeforeTwo!";
        if (!expectedSb.toString().equals(actual)) {
            throw new AssertionException(expectedSb + " not equals " + actual);
        }
    }

    @Test
    public void testFour() {
        int actual = 4;
        if (expected != actual) {
            throw new AssertionException(expected + " != " + actual);
        }
    }
}
