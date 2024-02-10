package ru.otus.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.SomeTestService;
import ru.otus.SomeTestServiceImpl;
import ru.otus.annotations.Log;

public class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}

    public static SomeTestService createClass() {
        InvocationHandler handler = new ProxyDemo(new SomeTestServiceImpl());
        return (SomeTestService)
                Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {SomeTestService.class}, handler);
    }

    static class ProxyDemo implements InvocationHandler {

        private final SomeTestService service;
        private final Set<String> annotatedMethods;

        ProxyDemo(SomeTestService service) {
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
