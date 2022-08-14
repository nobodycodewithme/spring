package com.nobodycodewithme.springautoconfig.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class RedisCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return redisHostIsSet(context);
    }

    private boolean redisHostIsSet(ConditionContext conditionContext) {
        return conditionContext.getEnvironment().containsProperty("spring.redis.host");
    }
}
