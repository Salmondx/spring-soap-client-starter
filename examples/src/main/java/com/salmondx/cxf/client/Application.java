package com.salmondx.cxf.client;

import accountinfo.BankInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import com.salmondx.cxf.client.annotation.EnableSoapClients;

/**
 * Created by Salmondx on 09/09/16.
 */
@SpringBootApplication
@ImportResource("classpath:spring/ws.xml")
@ComponentScan(basePackages = {"com.salmondx"})
@EnableSoapClients
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public BankInfo bankInfo() {
        BankInfo bankInfo = new BankInfo();
        bankInfo.setAccountNumber("32143141414");
        bankInfo.setBankCode("12342");
        bankInfo.setSwift("JPMRGN");
        return bankInfo;
    }

    @Bean
    public Converter localDateConverter() {
        return new DateConverter();
    }

    @Bean
    public ConversionService conversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(localDateConverter());
        return conversionService;
    }
}
