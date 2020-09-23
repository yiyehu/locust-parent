package tech.yiyehu.locust.config.context.bean;


import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import tech.yiyehu.locust.config.annotation.Reference;
import tech.yiyehu.locust.config.annotation.Service;
import tech.yiyehu.locust.config.context.ReferenceBean;
import tech.yiyehu.locust.config.context.ServiceBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;


/**
 * Dubbo {@link Service @Service} Bean Builder
 *
 * @see Service
 * @see Reference
 * @see ServiceBean
 * @see ReferenceBean
 * @since 2.6.5
 */
public class ServiceBeanNameBuilder {

    private static final String SEPARATOR = ":";

    private final String interfaceClassName;

    private final Environment environment;

    private String version;

    private ServiceBeanNameBuilder(String interfaceClassName, Environment environment) {
        this.interfaceClassName = interfaceClassName;
        this.environment = environment;
    }

    private ServiceBeanNameBuilder(Class<?> interfaceClass, Environment environment) {
        this(interfaceClass.getName(), environment);
    }

    private ServiceBeanNameBuilder(Service service, Class<?> interfaceClass, Environment environment) {
        this(resolveInterfaceName(service, interfaceClass), environment);
        this.version(service.version());
    }

    private ServiceBeanNameBuilder(Reference reference, Class<?> interfaceClass, Environment environment) {
        this(resolveInterfaceName(reference, interfaceClass), environment);
        this.version(reference.version());
    }

    public static ServiceBeanNameBuilder create(Class<?> interfaceClass, Environment environment) {
        return new ServiceBeanNameBuilder(interfaceClass, environment);
    }

    public static ServiceBeanNameBuilder create(Service service, Class<?> interfaceClass, Environment environment) {
        return new ServiceBeanNameBuilder(service, interfaceClass, environment);
    }

    public static ServiceBeanNameBuilder create(Reference reference, Class<?> interfaceClass, Environment environment) {
        return new ServiceBeanNameBuilder(reference, interfaceClass, environment);
    }

    private static void append(StringBuilder builder, String value) {
        if (StringUtils.hasText(value)) {
            builder.append(value).append(SEPARATOR);
        }
    }

    public ServiceBeanNameBuilder version(String version) {
        this.version = version;
        return this;
    }

    public String build() {
        StringBuilder beanNameBuilder = new StringBuilder("ServiceBean").append(SEPARATOR);
        // Required
        append(beanNameBuilder, interfaceClassName);
        // Optional
        append(beanNameBuilder, version);
        // Build and remove last ":"
        String rawBeanName = beanNameBuilder.substring(0, beanNameBuilder.length() - 1);
        // Resolve placeholders
        return environment.resolvePlaceholders(rawBeanName);
    }

    public static String resolveInterfaceName(Annotation annotation, Class<?> defaultInterfaceClass)
            throws IllegalStateException {
        Class clazz = annotation.annotationType();
        String interfaceName = null;
        try {
            String invokeResult = (String) clazz.getDeclaredMethod("interfaceName",clazz).invoke(annotation);
            Class invokeClazz = (Class) clazz.getDeclaredMethod("interfaceClass",clazz).invoke(annotation);
            if (StringUtils.hasText(invokeResult)) {
                interfaceName = invokeResult;
            } else if (!void.class.equals(invokeClazz)) {
                interfaceName = invokeClazz.getName();
            } else if (defaultInterfaceClass.isInterface()) {
                interfaceName = defaultInterfaceClass.getName();
            } else {
                throw new IllegalStateException(
                        "The @Service or @Reference undefined interfaceClass or interfaceName, and the type "
                                + defaultInterfaceClass.getName() + " is not a interface.");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return interfaceName;
    }
}