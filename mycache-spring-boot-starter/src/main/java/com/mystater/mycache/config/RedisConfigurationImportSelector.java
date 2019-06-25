package com.mystater.mycache.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessorRegistrar;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 西门
 * @version 0.1.0
 * @description ${description}
 * @date 2019/6/24
 */
public class RedisConfigurationImportSelector implements ImportSelector {
    /**
     * 返回的类型为带有@configuration注解或者实现ImportBeanDefinitionRegistrar的接口
     */
    private static final String[] IMPORTS = new String[]{RedisConfigurationImportSelector.ConfigurationPropertiesBeanRegistrar.class.getName(), ConfigurationPropertiesBindingPostProcessorRegistrar.class.getName(),RedisImportConfiguration.class.getName()};

    RedisConfigurationImportSelector() {
    }
    @Override
    public String[] selectImports(AnnotationMetadata metadata) {
        return IMPORTS;
    }

    public static class ConfigurationPropertiesBeanRegistrar implements ImportBeanDefinitionRegistrar {
        public ConfigurationPropertiesBeanRegistrar() {
        }

        /**
         * 动态注入bean,详见https://blog.csdn.net/weixin_34357928/article/details/87584270
         * @param metadata
         * @param registry
         */
        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            this.getTypes(metadata).forEach((type) -> {
                this.register(registry, (ConfigurableListableBeanFactory)registry, type);
            });
        }

        private List<Class<?>> getTypes(AnnotationMetadata metadata) {
            MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(EnableLocalRedis.class.getName(), false);
            return this.collectClasses(attributes != null ? (List)attributes.get("value") : Collections.emptyList());
        }

        private List<Class<?>> collectClasses(List<?> values) {
            return (List)values.stream().flatMap((value) -> {
                return Arrays.stream((Object[])((Object[])value));
            }).map((o) -> {
                return (Class)o;
            }).filter((type) -> {
                return Void.TYPE != type;
            }).collect(Collectors.toList());
        }

        private void register(BeanDefinitionRegistry registry, ConfigurableListableBeanFactory beanFactory, Class<?> type) {
            String name = this.getName(type);
            if (!this.containsBeanDefinition(beanFactory, name)) {
                this.registerBeanDefinition(registry, name, type);
            }

        }

        private String getName(Class<?> type) {
            ConfigurationProperties annotation = (ConfigurationProperties)AnnotationUtils.findAnnotation(type, ConfigurationProperties.class);
            String prefix = annotation != null ? annotation.prefix() : "";
            return StringUtils.hasText(prefix) ? prefix + "-" + type.getName() : type.getName();
        }

        private boolean containsBeanDefinition(ConfigurableListableBeanFactory beanFactory, String name) {
            if (beanFactory.containsBeanDefinition(name)) {
                return true;
            } else {
                BeanFactory parent = beanFactory.getParentBeanFactory();
                return parent instanceof ConfigurableListableBeanFactory ? this.containsBeanDefinition((ConfigurableListableBeanFactory)parent, name) : false;
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
    }
}
