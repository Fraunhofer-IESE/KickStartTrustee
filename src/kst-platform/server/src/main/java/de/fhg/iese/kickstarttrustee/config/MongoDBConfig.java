package de.fhg.iese.kickstarttrustee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "de.fhg.iese.kickstarttrustee")
public class MongoDBConfig {}
