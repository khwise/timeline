package com.smilegate.configurations.spring;


import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.annotation.*;
import org.springframework.context.event.EventListener;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.config.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.*;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableMongoAuditing
@PropertySource("classpath:application.properties")
@EnableMongoRepositories(
        basePackages = {
        "com.smilegate.dataproviders.database.mongodb.write"
        },
        mongoTemplateRef = "mongoWriteTemplate"
)
public class MongoDBMasterConfig extends AbstractMongoClientConfiguration {
    private static final Logger logger = LogManager.getLogger(MongoDBMasterConfig.class);

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

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

    @Primary
    @Bean(name = "mongoWriteTemplate")
    public MongoTemplate mongoWriteTemplate(){
        return new MongoTemplate(mongoClient(),getDatabaseName());
    }


    @Primary
    @Bean(name = "mongoConverter")
    public MongoConverter mongoConverter() {
        MongoMappingContext mappingContext = new MongoMappingContext();
        mappingContext.setAutoIndexCreation(true);
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(new SimpleMongoClientDbFactory(mongoClient(),getDatabaseName()));
        return new MappingMongoConverter(dbRefResolver, mappingContext);
    }

    /**
     * TODO 현재는 동작하지 않음. 동작 방법 찾자..
     *
     * 어플리케이션 기동 후 몽고 @Document 에 기술한 인텍스 생성 진헹.
     * Spring Data MongoDB 3.x. 부터는 자동 인덱스 생성 비활성처리 될 예정
     * https://stackoverflow.com/questions/60003179/please-use-mongomappingcontextsetautoindexcreationboolean-or-override-mong
     *
     * Spring Shell 어플리케이션은 ApplicationRunner를 사용하기때문에 ApplicationReadyEvent는 발생하지 않음
     * 대신 ApplicationPreparedEvent 사용 가능.
     */
    @EventListener(ApplicationPreparedEvent.class)
    public void initIndicesAfterStartup() {
        logger.info("Mongo InitIndicesAfterStartup init");
        final long init = System.currentTimeMillis();

        MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = mongoConverter().getMappingContext();

        if (mappingContext instanceof MongoMappingContext) {
            MongoMappingContext mongoMappingContext = (MongoMappingContext) mappingContext;
            for (BasicMongoPersistentEntity<?> persistentEntity : mongoMappingContext.getPersistentEntities()) {
                Class clazz = persistentEntity.getType();
                if (clazz.isAnnotationPresent(Document.class)) {
                    MongoPersistentEntityIndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);

                    IndexOperations indexOps = mongoWriteTemplate().indexOps(clazz);
                    resolver.resolveIndexFor(clazz).forEach(indexOps::ensureIndex);
                }
            }
        }
        logger.info("Mongo InitIndicesAfterStartup take: {}ms", ()->(System.currentTimeMillis() - init));
    }
}
