package tech.yiyehu.locust.config.context.bean;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.core.env.PropertyResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.util.CollectionUtils.arrayToList;

public class AnnotationPropertyValuesAdapter extends HashMap<String, PropertyValue> implements PropertyValues {

    String[] ignoreAttributeNames;
    Annotation annotation;

    public AnnotationPropertyValuesAdapter(Annotation annotation, String[] ignoreAttributeNames) {
        this.annotation = annotation;
        this.ignoreAttributeNames = ignoreAttributeNames;
        this.putAll(getPropertyValues(annotation, ignoreAttributeNames));
    }

    private Map<String, ? extends PropertyValue> getPropertyValues(Annotation annotation, String[] ignoreAttributeNames) {
        Set<String> ignoreAttributeNamesSet = new HashSet<String>(arrayToList(ignoreAttributeNames));
        Class<? extends Annotation> clazz = annotation.annotationType();
        Map<String, PropertyValue> actualAttributes = new HashMap<>();
        for (Method method : clazz.getMethods()) {
            if (ignoreAttributeNamesSet.contains(method.getName())) {
                continue;
            }
            try {
                Object attributeValue = method.invoke(annotation);
                Object defaultValue = method.getDefaultValue();
                if (attributeValue != null) {
                    actualAttributes.put(method.getName(), new PropertyValue(method.getName(), attributeValue));
                } else if (defaultValue != null) {
                    actualAttributes.put(method.getName(), new PropertyValue(method.getName(), defaultValue));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return actualAttributes;
    }

    @Override
    public PropertyValue[] getPropertyValues() {
        return this.values().toArray(new PropertyValue[this.size()]);
    }

    @Override
    public PropertyValue getPropertyValue(String propertyName) {
        return this.get(propertyName);
    }

    @Override
    public PropertyValues changesSince(PropertyValues old) {
        return null;
    }

    @Override
    public boolean contains(String propertyName) {
        return this.contains(propertyName);
    }

    @Override
    public boolean isEmpty() {
        return this.isEmpty();
    }
}
