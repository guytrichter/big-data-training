package org.trichter.mongo.configuration;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by guy on 12/6/16.
 */
@Configuration
public class MongoConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port}")
    private int mongoPort;

//    @Bean
//    public MongoTemplate mongoTemplate() throws Exception {
//        return new MongoTemplate(new MongoClient(mongoHost, mongoPort),"listings");
//    }


    public int getMongoPort() {
        return mongoPort;
    }

    public String getMongoHost() {
        return mongoHost;
    }
}
