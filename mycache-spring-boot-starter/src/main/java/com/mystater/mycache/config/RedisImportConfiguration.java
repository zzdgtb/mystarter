package com.mystater.mycache.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author 西门
 * @version 0.1.0
 * @description ${description}
 * @date 2019/6/24
 */
public class RedisImportConfiguration implements ImportBeanDefinitionRegistrar, BeanFactoryAware, EnvironmentAware {
    /**
     * 可以获取环境参数
     */
    private Environment environment;
    /**
     * 可以获取beanFactory中的参数
     */
    private BeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableLocalRedis.class.getName()));
        RedisProperties redisProperties = (RedisProperties)this.beanFactory.getBean(attributes.getClassArray("value")[0]);
        BeanDefinition redisBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(RedisAutoConfiguration.class).addConstructorArgValue(redisProperties)
                .getBeanDefinition();
        registry.registerBeanDefinition("redisAutoConfiguration", redisBeanDefinition);
    }

    private void register(BeanDefinitionRegistry registry, ConfigurableListableBeanFactory beanFactory, Class<?> type) {
        String name = this.getName(type);
        if (!this.containsBeanDefinition(beanFactory, name)) {
            this.registerBeanDefinition(registry, name, type);
        }

    }
    private void registerBeanDefinition(BeanDefinitionRegistry registry, String name, Class<?> type) {
        this.assertHasAnnotation(type);
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(type);
        registry.registerBeanDefinition(name, definition);
    }
    private void assertHasAnnotation(Class<?> type) {
        Assert.notNull(AnnotationUtils.findAnnotation(type, ConfigurationProperties.class), () -> {
            return "No " + ConfigurationProperties.class.getSimpleName() + " annotation found on  '" + type.getName() + "'.";
        });
    }

    private boolean containsBeanDefinition(ConfigurableListableBeanFactory beanFactory, String name) {
        if (beanFactory.containsBeanDefinition(name)) {
            return true;
        } else {
            BeanFactory parent = beanFactory.getParentBeanFactory();
            return parent instanceof ConfigurableListableBeanFactory ? this.containsBeanDefinition((ConfigurableListableBeanFactory)parent, name) : false;
        }
    }
    private String getName(Class<?> type) {
        ConfigurationProperties annotation = (ConfigurationProperties) AnnotationUtils.findAnnotation(type, ConfigurationProperties.class);
        String prefix = annotation != null ? annotation.prefix() : "";
        return StringUtils.hasText(prefix) ? prefix + "-" + type.getName() : type.getName();
    }
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
