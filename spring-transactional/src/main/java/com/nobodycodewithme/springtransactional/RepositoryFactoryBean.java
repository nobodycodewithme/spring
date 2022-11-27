package com.nobodycodewithme.springtransactional;

import com.nobodycodewithme.springtransactional.entity.Greeting;
import com.nobodycodewithme.springtransactional.proxy.TransactionalInvocationHandler;
import com.nobodycodewithme.springtransactional.repository.GreetingRepository;
import com.nobodycodewithme.springtransactional.repository.Repository;
import com.nobodycodewithme.springtransactional.repository.UserRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;

@Getter
@Setter
public class RepositoryFactoryBean implements FactoryBean<Repository<?, ?>>, ApplicationContextAware {
    private Class<?> type;
    private boolean isSingleton = true;
    private ApplicationContext applicationContext;

    @Override
    public Repository<?, ?> getObject() throws Exception {
        Repository<?, ?> instance = (Repository<?, ?>) type.getDeclaredConstructor().newInstance();

        // tu dong inject dependencies cho doi tuong instance
        applicationContext.getAutowireCapableBeanFactory().autowireBean(instance);

        TransactionalInvocationHandler transactionalInvocationHandler = new TransactionalInvocationHandler(instance);
        Repository<?, ?> repositoryProxy = (Repository<?, ?>) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Repository.class}, transactionalInvocationHandler);

        return repositoryProxy;
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public boolean isSingleton() {
        return isSingleton;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
