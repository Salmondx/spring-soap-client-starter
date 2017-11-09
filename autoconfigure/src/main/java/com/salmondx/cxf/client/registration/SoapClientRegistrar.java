package com.salmondx.cxf.client.registration;

import com.salmondx.cxf.client.annotation.SoapClient;
import com.salmondx.cxf.client.registration.model.SoapMethodData;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import com.salmondx.cxf.client.annotation.SoapMethod;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created by Salmondx on 01/09/16.
 */
public class SoapClientRegistrar implements ImportBeanDefinitionRegistrar,
        ResourceLoaderAware, BeanClassLoaderAware {

    private ResourceLoader resourceLoader;

    private ClassLoader classLoader;

    public SoapClientRegistrar() {}

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        registerSoapClients(metadata, registry);
    }

    public void registerSoapClients(AnnotationMetadata metadata,
                                     BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);

        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(
                SoapClient.class);
        scanner.addIncludeFilter(annotationTypeFilter);

        Set<String> basePackages = getBasePackages(metadata);

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);

            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    // verify annotated class is an interface
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    Assert.isTrue(annotationMetadata.isInterface(),
                            "@SoapClient can only be specified on an interface");

                    Map<String, Object> attributes = annotationMetadata
                            .getAnnotationAttributes(
                                    SoapClient.class.getCanonicalName());
                    registerSoapClient(registry, annotationMetadata, attributes);
                }
            }
        }
    }

    private void registerSoapClient(BeanDefinitionRegistry registry,
                                    AnnotationMetadata annotationMetadata,
                                    Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(SoapClientFactoryBean.class);
        validate(attributes);

        Class<?> serviceType = getClientService(attributes);
        // Type name of annotated interface
        definition.addPropertyValue("type", getClassByName(className));
        // Soap service type
        definition.addPropertyValue("service", serviceType);
        definition.addPropertyValue("soapMethods", getAnnotatedMethods(annotationMetadata));
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        String alias = className + "SoapClient";
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        beanDefinition.setPrimary(true);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className,
                new String[] { alias });
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private Class<?> getClassByName(String name) {
        return ClassUtils.resolveClassName(name, SoapClientRegistrar.this.classLoader);
    }

    private Class<?> getClientService(Map<String, Object> client) {
        if (client == null) {
            return null;
        }
        return (Class<?>) client.get("service");
    }

    private Map<String, List<SoapMethodData>> getAnnotatedMethods(AnnotationMetadata metadata) {
        String annotationName = SoapMethod.class.getCanonicalName();
        Map<String, List<SoapMethodData>> soapMethods = new HashMap<>();
        Set<MethodMetadata> proxyInterfaceMethods = metadata.getAnnotatedMethods(annotationName);
        for (MethodMetadata proxyInterfaceMethod : proxyInterfaceMethods) {
            String soapMethodName = (String) proxyInterfaceMethod.getAnnotationAttributes(annotationName).get("value");
            Class<?>[] autowiredFields = (Class<?>[]) proxyInterfaceMethod.getAnnotationAttributes(annotationName).get("autowired");
            Assert.hasText(soapMethodName, "Soap method name cannot be null or empty");
            if (soapMethods.containsKey(soapMethodName)) {
                soapMethods.get(soapMethodName).add(new SoapMethodData(
                        proxyInterfaceMethod.getMethodName(),
                        proxyInterfaceMethod.getReturnTypeName(),
                        autowiredFields));
            } else {
                List<SoapMethodData> methodNames = new ArrayList<>();
                methodNames.add(new SoapMethodData(proxyInterfaceMethod.getMethodName(),
                        proxyInterfaceMethod.getReturnTypeName(),
                        autowiredFields));
                soapMethods.put(soapMethodName, methodNames);
            }
        }
        return soapMethods;
    }

    private void validate(Map<String, Object> attributes) {
        Assert.notNull(attributes.get("service"),
                "Service field should not be null");
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Set<String> basePackages = new HashSet<>();
        basePackages.add(
                ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        return basePackages;
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false) {

            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (beanDefinition.getMetadata().isInterface()
                            && beanDefinition.getMetadata()
                            .getInterfaceNames().length == 1
                            && Annotation.class.getName().equals(beanDefinition
                            .getMetadata().getInterfaceNames()[0])) {
                        try {
                            Class<?> target = ClassUtils.forName(
                                    beanDefinition.getMetadata().getClassName(),
                                    SoapClientRegistrar.this.classLoader);
                            return !target.isAnnotation();
                        }
                        catch (Exception ex) {
                            this.logger.error(
                                    "Could not load target class: "
                                            + beanDefinition.getMetadata().getClassName(),
                                    ex);
                        }
                    }
                    return true;
                }
                return false;

            }
        };
    }
}
