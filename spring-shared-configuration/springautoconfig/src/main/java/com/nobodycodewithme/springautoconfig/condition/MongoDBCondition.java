package com.nobodycodewithme.springautoconfig.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MongoDBCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return databaseUrlIsSet(context) && mongoDBDriverExistsOnClassPath();
    }

    private boolean databaseUrlIsSet(ConditionContext conditionContext) {
        return conditionContext.getEnvironment().containsProperty("spring.data.mongodb.uri");
    }

    private boolean mongoDBDriverExistsOnClassPath() {
        try {
            Class.forName("com.mongodb.client.MongoClient");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
