package org.sergeydevjava.beanpostprocessor;

import org.sergeydevjava.annotation.LogExecutionTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Component
@Profile("log-exec-time")
@ConditionalOnProperty(prefix = "link-shortener.logging", value = "enable-log-execution-time", havingValue = "true")
public class LogExecutionTimeBeanPostProcessor implements BeanPostProcessor {

    private static Logger log = LoggerFactory.getLogger(LogExecutionTimeBeanPostProcessor.class);

    private final Map<String, BeanMethodsData> beanMethodsDataByBeanName = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();

        for (Method method : methods) {
            boolean annotationPresent = method.isAnnotationPresent(LogExecutionTime.class);
            if (annotationPresent) {
                beanMethodsDataByBeanName
                        .computeIfAbsent(beanName, bn -> new BeanMethodsData(bean.getClass(), new ArrayList<>()))
                        .annotatedMethods.add(method);
            }
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        BeanMethodsData beanMethodsData = beanMethodsDataByBeanName.get(beanName);

        if (isNull(beanMethodsData)) {
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }

        Class<?> clazz = beanMethodsData.clazz();
        List<Method> methods = beanMethodsData.annotatedMethods();


        return Proxy.newProxyInstance(clazz.getClassLoader(), bean.getClass().getInterfaces(), (proxy, method, args) -> {
            boolean methodIsAnnotated = methods.stream().anyMatch(pojoMethod -> areMethodsEqual(pojoMethod, method));

            if (methodIsAnnotated) {
                Instant start = Instant.now();
                try {
                    return method.invoke(bean, args);
                } catch (Exception e) {
                    throw e.getCause();
                } finally {
                    Instant finish = Instant.now();
                    long timeElapsed = Duration.between(start, finish).toNanos();
                    log.info("Время выполнения метода {}: {}", method.getName(), timeElapsed);
                }
            }

            try {
                return method.invoke(bean, args);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    private boolean areMethodsEqual(Method one, Method other) {
        if (one.getName().equals(other.getName())) {
            return equalParamTypes(one.getParameterTypes(), other.getParameterTypes());
        }
        return false;
    }

    private boolean equalParamTypes(Class<?>[] params1, Class<?>[] params2) {
        if (params1.length == params2.length) {
            for (int i = 0; i < params1.length; ++i) {
                if (params1[i] != params2[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private record BeanMethodsData(Class<?> clazz, List<Method> annotatedMethods) {
    }
}
