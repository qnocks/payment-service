package com.itransition.payment.core.util;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BeansUtils {

    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Object value = beanWrapper.getPropertyValue(propertyDescriptor.getName());
            if (value == null)
                emptyNames.add(propertyDescriptor.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
