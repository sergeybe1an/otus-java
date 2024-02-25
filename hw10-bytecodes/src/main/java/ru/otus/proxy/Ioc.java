package ru.otus.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.Log;

/**
 * Ioc-container
 */
public class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}

    @SuppressWarnings("unchecked")
    public static <T> T createClass(Class<?> clazz, Object... args) {
        InvocationHandler handler = new ProxyDemo<>(instantiate(clazz, args));

        return (T) Proxy.newProxyInstance(
            Ioc.class.getClassLoader(),
            clazz.getInterfaces(),
            handler
        );
    }

    /**
     * Create object with reflection
     * @param type created data type class
     * @param args constructor args
     * @param <T> created data type
     * @return new object with T data type
     */
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

     private static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
     }

    static class ProxyDemo<T> implements InvocationHandler {

        private final T service;
        private final Set<String> annotatedMethods;

        ProxyDemo(T service) {
            this.service = service;
            this.annotatedMethods = Arrays.stream(service.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(Log.class))
                .map(this::getMethodName)
                .collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (annotatedMethods.contains(getMethodName(method))) {
                logger.info("executed method: {}, param(s): {}", method, concatMethodParamValues(args));
            }

            return method.invoke(service, args);
        }

        private String concatMethodParamValues(Object[] args) {
            return Arrays.stream(args)
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        }

        private String getMethodName(Method method) {
            return method.getName() + method.getParameterCount();
        }
    }
}
