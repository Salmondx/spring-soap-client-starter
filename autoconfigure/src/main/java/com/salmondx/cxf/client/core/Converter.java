package com.salmondx.cxf.client.core;

import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;

/**
 * Created by Salmondx on 08/09/16.
 */
public class Converter implements TypeConverter {
    private ApplicationContext applicationContext;
    private ConversionService conversionService;

    public Converter(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        if (conversionService == null) {
            this.conversionService = applicationContext.getBean(ConversionService.class);
        }
        return conversionService.canConvert(sourceType, targetType);
    }

    public Object convert(Object source, Class<?> targetType) {
        if (conversionService == null) {
            this.conversionService = applicationContext.getBean(ConversionService.class);
        }
        if (source == null) {
            return source;
        }
        return conversionService.convert(source, targetType);
    }

    public Object convertIfDifferentTypes(Object source, Class<?> target) {
        if (source == null) {
            return source;
        }
        if (!source.getClass().isAssignableFrom(target.getClass())){
            return convert(source, target);
        }
        return source;
    }
}
