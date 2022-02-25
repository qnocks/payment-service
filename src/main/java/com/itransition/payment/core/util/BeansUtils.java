package com.itransition.payment.core.util;

import java.util.HashSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.beans.BeanWrapperImpl;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BeansUtils {

    public static String[] getNullPropertyNames(final Object source) {
        val beanWrapper = new BeanWrapperImpl(source);
        val propertyDescriptors = beanWrapper.getPropertyDescriptors();
        val emptyNames = new HashSet<String>();

        for (var propertyDescriptor : propertyDescriptors) {
            val value = beanWrapper.getPropertyValue(propertyDescriptor.getName());
            if (value == null) {
                emptyNames.add(propertyDescriptor.getName());
            }
        }

        val result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
