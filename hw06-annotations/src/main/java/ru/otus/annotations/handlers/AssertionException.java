package ru.otus.annotations.handlers;

public class AssertionException extends RuntimeException {

    public AssertionException() {
        super();
    }

    public AssertionException(String message) {
        super(message);
    }
}
