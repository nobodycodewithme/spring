package com.nobodycodewithme.springtransactional;

import com.nobodycodewithme.springtransactional.annotation.CustomRepository;
import com.nobodycodewithme.springtransactional.annotation.EnableCustomRepository;
import com.nobodycodewithme.springtransactional.repository.Repository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RepositoryImportRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
    private ResourceLoader resourceLoader;
    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        // 1
        if (!importingClassMetadata.hasAnnotation(EnableCustomRepository.class.getName())) {
            return;
        }
        // 2
        Map<String, Object> annotationAttributesMap = importingClassMetadata.getAnnotationAttributes(EnableCustomRepository.class.getName());
        AnnotationAttributes annotationAttributes = Optional.ofNullable(AnnotationAttributes.fromMap(annotationAttributesMap))
                .orElseGet(AnnotationAttributes::new);
        String[] packages = getPackages(importingClassMetadata, annotationAttributes);

        // 3
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false, environment, resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter((CustomRepository.class)));
//        scanner.addIncludeFilter(new AssignableTypeFilter(Repository.class));

        // 4
        for (String packageScan : packages) {
            Set<BeanDefinition> candidateBeanDefinition = scanner.findCandidateComponents(packageScan);
            try {
                registerBeanDefinition(registry, candidateBeanDefinition);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private String[] getPackages(AnnotationMetadata annotationMetadata, AnnotationAttributes annotationAttributes) {
        String[] packages = annotationAttributes.getStringArray("packages");
        if (packages.length > 0) {
            return packages;
        }
        String classNameContainAnnotation = annotationMetadata.getClassName();
        int lastIndexOfDot = classNameContainAnnotation.lastIndexOf(".");
        return new String[]{classNameContainAnnotation.substring(0, lastIndexOfDot)};
    }

    private void registerBeanDefinition(BeanDefinitionRegistry beanDefinitionRegistry, Set<BeanDefinition> beanDefinitions) throws ClassNotFoundException {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
                AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();
                Map<String, Object> annotationAttributesMap = annotationMetadata.getAnnotationAttributes(CustomRepository.class.getName());
                AnnotationAttributes annotationAttributes = Optional.ofNullable(AnnotationAttributes.fromMap(annotationAttributesMap)).orElseGet(AnnotationAttributes::new);
                String beanName = null;
                boolean isSingleton = true;
                if (!annotationAttributes.isEmpty()) {
                    beanName = annotationAttributes.getString("beanName");
                    isSingleton = annotationAttributes.getBoolean("singleton");
                }
                if (!StringUtils.hasText(beanName)) {
                    beanName = StringUtils.uncapitalize(annotationMetadata.getClassName().substring(annotationMetadata.getClassName().lastIndexOf(".") + 1));
                }

                String className = annotationMetadata.getClassName();

                AbstractBeanDefinition abstractBeanDefinition = BeanDefinitionBuilder
                        .genericBeanDefinition(RepositoryFactoryBean.class)
                        .addPropertyValue("type", Class.forName(className)) // call method setType in RepositoryFactoryBean.class
                        .addPropertyValue("singleton", isSingleton) // call method setSingleton in RepositoryFactoryBean.class
                        .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                        .setRole(BeanDefinition.ROLE_INFRASTRUCTURE)
                        .getBeanDefinition();

                beanDefinitionRegistry.registerBeanDefinition(beanName, abstractBeanDefinition);
            }
        }
    }
}
