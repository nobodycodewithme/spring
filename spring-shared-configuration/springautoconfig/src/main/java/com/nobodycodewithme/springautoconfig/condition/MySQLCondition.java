package com.nobodycodewithme.springautoconfig.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MySQLCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return databaseUrlIsSet(context) && mySqlDriverExistsOnClassPath();
    }

    private boolean databaseUrlIsSet(ConditionContext conditionContext) {
        return conditionContext.getEnvironment().containsProperty("spring.datasource.url");
    }

    private boolean mySqlDriverExistsOnClassPath() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
