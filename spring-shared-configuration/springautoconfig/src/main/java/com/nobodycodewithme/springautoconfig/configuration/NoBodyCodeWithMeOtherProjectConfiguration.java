package com.nobodycodewithme.springautoconfig.configuration;

import com.nobodycodewithme.springautoconfig.condition.NoBodyCodeWithMeConfigurationCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Conditional(NoBodyCodeWithMeConfigurationCondition.class)
@Import(NoBodyCodeWithMeShareConfiguration.class) // (1)
public class NoBodyCodeWithMeOtherProjectConfiguration {
    // (2)
    // cấu hình các bean khác
}
