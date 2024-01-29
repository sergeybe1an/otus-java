package ru.otus.annotations.handlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestProcessing {

    private static Logger logger = LoggerFactory.getLogger(TestProcessing.class.getName());
    private static Integer SUCCESS = 1;
    private static Integer ERROR = -1;

    public static void process(String className) {
        Class<?> clazz = init(className);

        List<Method> beforeMethods = getMethodsByAnnotation(clazz, Before.class);
        List<Method> testMethods = getMethodsByAnnotation(clazz, Test.class);
        List<Method> afterMethods = getMethodsByAnnotation(clazz, After.class);
        List<Method> failedMethods = new ArrayList<>(Collections.emptyList());

        testMethods.forEach(testMethod -> {
            Object currentObj = instantiate(clazz);

            boolean beforeMethodsResult = beforeMethods.stream()
                .map(beforeMethod -> invokeMethod(currentObj, beforeMethod))
                .anyMatch(status -> status == ERROR);

            if (!beforeMethodsResult && invokeMethod(currentObj, testMethod) == SUCCESS) {
                logger.info(testMethod.getName() + " successfully passed!");
            } else {
                failedMethods.add(testMethod);
            }

            afterMethods.forEach(afterMethod -> invokeMethod(currentObj, afterMethod));
        });

        printTestStatistic(testMethods.size(), failedMethods);
    }

    private static Object invokeMethod(Object object, Method method) {
        try {
            method.setAccessible(true);
            method.invoke(object);

            return SUCCESS;
        } catch (InvocationTargetException ae) {
            logger.error(method.getName() + " failed: " + ae.getTargetException().getMessage());
            return ERROR;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Method> getMethodsByAnnotation(Class<?> type, Class<? extends Annotation> annotation) {
        return Arrays.stream(type.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(annotation))
            .toList();
    }

    private static Class<?> init(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class with name " + className + " not found!", e);
        }
    }

    private static void printTestStatistic(int allMethodsSize, Collection<? extends Method> failedMethods) {
        logger.info("All tests quantity: " + allMethodsSize +
                    ", Failed tests quantity: " + failedMethods.size());

        if (!failedMethods.isEmpty()) {
            logger.info("Failed tests: " + failedMethods
                .stream()
                .map(Method::getName)
                .collect(Collectors.joining(", ")));
        }
    }

    private static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }
}
