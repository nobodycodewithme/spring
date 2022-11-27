package com.nobodycodewithme.springtransactional.proxy;

import com.nobodycodewithme.springtransactional.annotation.CustomTransactional;
import com.nobodycodewithme.springtransactional.repository.Repository;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ToString
public class TransactionalInvocationHandler implements InvocationHandler {
    private final Repository<?, ?> target;
    private final Map<String, Method> methods;

    public TransactionalInvocationHandler(Repository<?, ?> target) {
        this.target = target;
        methods = new HashMap<>();
        for (Method method : target.getClass().getDeclaredMethods()) {
            methods.putIfAbsent(method.getName(), method);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method originalMethod = methods.get(method.getName());
        log.info("method: {}, arguments: {}", originalMethod.getDeclaringClass() + "." +method.getName(), args);
        if (applyTransactionForAllMethod() || originalMethod.isAnnotationPresent(CustomTransactional.class)) {

            log.info("@Transactional is present, opening transaction to db");
            try {
                log.info("invoking method on target");
                Object returnValue = originalMethod.invoke(target, args);
                log.info("committing transaction");
                return returnValue;
            } catch (Exception e) {
                log.error("has exception in method: {}, error message: {}, rollback transaction", originalMethod.getName(), e.getMessage());
                throw e;
            }
        } else {
            log.info("@Transactional is not present, call method directly");
            return originalMethod.invoke(target, args);
        }
    }

    private boolean applyTransactionForAllMethod() {
        return target.getClass().getAnnotation(CustomTransactional.class) != null;
    }
}
