package com.smilegate.configurations.spring;


import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.config.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableMongoAuditing
@PropertySource("classpath:application.properties")
@EnableMongoRepositories(value = {
        "com.smilegate.dataproviders.database.mongodb.write"
})
public class MongoDBMasterConfig extends AbstractMongoClientConfiguration {
    @Value("${config.mongo.dbname}")
    private String dbName;

    @Value("${config.mongo.url}")
    private String mongoUrl;

    @Value("${config.mongos.user}")
    private String mongosUser;

    @Value("${config.mongos.password}")
    private String mongosPassword;

    @Value("${config.mongos.cluter}")
    private Boolean isCluster;

    @Value("${config.mongo.write.pool.size}")
    private Integer poolSize;

    @Override
    public MongoClient mongoClient() {

        // https://mongodb.github.io/mongo-java-driver/3.12/driver/tutorials/connect-to-mongodb/
        MongoClientSettings.Builder settings = MongoClientSettings.builder()
                .applyConnectionString(
                        new ConnectionString(mongoUrl)
                )
                .applyToServerSettings(
                        builder -> builder
                        .heartbeatFrequency(30, TimeUnit.SECONDS)
                )
                .applyToConnectionPoolSettings(
                        builder -> builder
                        .maxConnectionIdleTime(10, TimeUnit.MINUTES)
                        .maxConnectionLifeTime(1, TimeUnit.HOURS)
                        //.maintenanceInitialDelay(10, TimeUnit.SECONDS)
                        .maxWaitQueueSize(100)
                        .maxSize(poolSize)
                        .minSize(poolSize)
                )
                .applyToSocketSettings(
                        builder -> builder
                        .connectTimeout(8, TimeUnit.SECONDS)
                        .readTimeout(40, TimeUnit.SECONDS)
                )
                .applyToSslSettings(
                        builder -> builder
                        .invalidHostNameAllowed(true)
                )
                .compressorList(Arrays.asList(MongoCompressor.createZstdCompressor()))
                .writeConcern(WriteConcern.MAJORITY)
                .retryWrites(false)
                //.retryReads(true)
                .applicationName("timeline-write")
                .credential(
                        MongoCredential.createCredential(
                                !"".equals(mongosUser)?mongosUser:null,
                                dbName,
                                !"".equals(mongosPassword)?mongosPassword.toCharArray():null
                        )
                );

        if(isCluster){
            if(mongoUrl.contains(",")){
                settings.readPreference(ReadPreference.secondary());
            }else{
                settings.readPreference(ReadPreference.secondaryPreferred());
            }
        }

        return MongoClients.create(settings.build());
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Primary
    @Bean(name = "mongoTemplate4Write")
    public MongoTemplate mongoTemplate4Write(){
        return new MongoTemplate(mongoClient(),getDatabaseName());
    }
}
