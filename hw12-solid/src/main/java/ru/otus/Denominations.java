package ru.otus;

/**
 * Номиналы купюр
 */
public enum Denominations {
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000),
    TWO_THOUSANDS(2000),
    FIVE_THOUSANDS(5000);

    private final int value;
    Denominations(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
