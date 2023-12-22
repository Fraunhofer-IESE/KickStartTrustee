package de.fhg.iese.kickstarttrustee.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import de.fhg.iese.kickstarttrustee.common.properties.IDataItemProperties;
import de.fhg.iese.kickstarttrustee.properties.DataItemProperties;

@Configuration
public class DataItemConfig {
    @Bean
	@Validated
	@ConfigurationProperties("data-items")
    IDataItemProperties dataItemProperties() {
        return new DataItemProperties();
    }
}
