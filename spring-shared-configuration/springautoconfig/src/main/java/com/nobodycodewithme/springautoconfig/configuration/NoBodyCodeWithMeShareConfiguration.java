package com.nobodycodewithme.springautoconfig.configuration;

import com.nobodycodewithme.springautoconfig.caching.NoBodyCodeWithMeCaching;
import com.nobodycodewithme.springautoconfig.caching.NoBodyCodeWithMeRedisCaching;
import com.nobodycodewithme.springautoconfig.condition.MongoDBCondition;
import com.nobodycodewithme.springautoconfig.condition.MySQLCondition;
import com.nobodycodewithme.springautoconfig.condition.RedisCondition;
import com.nobodycodewithme.springautoconfig.database.NoBodyCodeWithMeDatabase;
import com.nobodycodewithme.springautoconfig.database.NoBodyCodeWithMeMongoDB;
import com.nobodycodewithme.springautoconfig.database.NoBodyCodeWithMeMySql;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoBodyCodeWithMeShareConfiguration {
    @Bean
    @Conditional(MySQLCondition.class)
    NoBodyCodeWithMeDatabase noBodyCodeWithMeMySqlDatabase() {
        return new NoBodyCodeWithMeMySql();
    }

    @Bean
    @Conditional(MongoDBCondition.class)
    NoBodyCodeWithMeDatabase noBodyCodeWithMeMongoDBDatabase() {
        return new NoBodyCodeWithMeMongoDB();
    }

    @Bean
    @Conditional(RedisCondition.class)
    NoBodyCodeWithMeCaching noBodyCodeWithMeCaching() {
        return new NoBodyCodeWithMeRedisCaching();
    }
}
