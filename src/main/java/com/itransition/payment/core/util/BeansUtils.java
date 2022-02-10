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

    public static String[] getNullPropertyNames(final Object source) {
        var beanWrapper = new BeanWrapperImpl(source);
        var propertyDescriptors = beanWrapper.getPropertyDescriptors();
        var emptyNames = new HashSet<String>();

        for (var propertyDescriptor : propertyDescriptors) {
            var value = beanWrapper.getPropertyValue(propertyDescriptor.getName());
            if (value == null)
                emptyNames.add(propertyDescriptor.getName());
        }

        var result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
