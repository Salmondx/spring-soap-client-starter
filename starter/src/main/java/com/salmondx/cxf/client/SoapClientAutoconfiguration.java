package com.salmondx.cxf.client;

import com.salmondx.cxf.client.annotation.EnableSoapClients;
import com.salmondx.cxf.client.core.Converter;
import com.salmondx.cxf.client.core.TypeConverter;
import com.salmondx.cxf.client.core.metadata.request.RequestMetadataFactory;
import com.salmondx.cxf.client.core.metadata.response.ResponseMetadataFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Salmondx on 09/09/16.
 */
@Configuration
@ConditionalOnClass(EnableSoapClients.class)
public class SoapClientAutoconfiguration {
    @Bean
    @ConditionalOnMissingBean(TypeConverter.class)
    public TypeConverter converter(ApplicationContext applicationContext) {
        return new Converter(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean(RequestMetadataFactory.class)
    public RequestMetadataFactory requestMetadataFactory(TypeConverter converter) {
        return new RequestMetadataFactory.Default(converter);
    }

    @Bean
    @ConditionalOnMissingBean(ResponseMetadataFactory.class)
    public ResponseMetadataFactory responseMetadataFactory(TypeConverter converter) {
        return new ResponseMetadataFactory.Default(converter);
    }
}
