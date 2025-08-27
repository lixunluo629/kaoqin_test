package org.springframework.boot.autoconfigure.data.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.Collections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@EnableConfigurationProperties({MongoProperties.class})
@Configuration
@ConditionalOnClass({MongoClient.class, MongoTemplate.class})
@AutoConfigureAfter({MongoAutoConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/mongo/MongoDataAutoConfiguration.class */
public class MongoDataAutoConfiguration {
    private final ApplicationContext applicationContext;
    private final MongoProperties properties;

    public MongoDataAutoConfiguration(ApplicationContext applicationContext, MongoProperties properties) {
        this.applicationContext = applicationContext;
        this.properties = properties;
    }

    @ConditionalOnMissingBean({MongoDbFactory.class})
    @Bean
    public SimpleMongoDbFactory mongoDbFactory(MongoClient mongo) throws Exception {
        String database = this.properties.getMongoClientDatabase();
        return new SimpleMongoDbFactory(mongo, database);
    }

    @ConditionalOnMissingBean
    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MongoConverter converter) throws UnknownHostException {
        return new MongoTemplate(mongoDbFactory, converter);
    }

    @ConditionalOnMissingBean({MongoConverter.class})
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory, CustomConversions conversions) {
        MappingMongoConverter mappingConverter = new MappingMongoConverter(new DefaultDbRefResolver(factory), context);
        mappingConverter.setCustomConversions(conversions);
        return mappingConverter;
    }

    @ConditionalOnMissingBean
    @Bean
    public MongoMappingContext mongoMappingContext(BeanFactory beanFactory, CustomConversions conversions) throws ClassNotFoundException {
        MongoMappingContext context = new MongoMappingContext();
        context.setInitialEntitySet(new EntityScanner(this.applicationContext).scan(Document.class, Persistent.class));
        Class<?> strategyClass = this.properties.getFieldNamingStrategy();
        if (strategyClass != null) {
            context.setFieldNamingStrategy((FieldNamingStrategy) BeanUtils.instantiate(strategyClass));
        }
        context.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
        return context;
    }

    @ConditionalOnMissingBean
    @Bean
    public GridFsTemplate gridFsTemplate(MongoDbFactory mongoDbFactory, MongoTemplate mongoTemplate) {
        return new GridFsTemplate(new GridFsMongoDbFactory(mongoDbFactory, this.properties), mongoTemplate.getConverter());
    }

    @ConditionalOnMissingBean
    @Bean
    public CustomConversions mongoCustomConversions() {
        return new CustomConversions(Collections.emptyList());
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/mongo/MongoDataAutoConfiguration$GridFsMongoDbFactory.class */
    private static class GridFsMongoDbFactory implements MongoDbFactory {
        private final MongoDbFactory mongoDbFactory;
        private final MongoProperties properties;

        GridFsMongoDbFactory(MongoDbFactory mongoDbFactory, MongoProperties properties) {
            Assert.notNull(mongoDbFactory, "MongoDbFactory must not be null");
            Assert.notNull(properties, "Properties must not be null");
            this.mongoDbFactory = mongoDbFactory;
            this.properties = properties;
        }

        public DB getDb() throws DataAccessException {
            String gridFsDatabase = this.properties.getGridFsDatabase();
            if (StringUtils.hasText(gridFsDatabase)) {
                return this.mongoDbFactory.getDb(gridFsDatabase);
            }
            return this.mongoDbFactory.getDb();
        }

        public DB getDb(String dbName) throws DataAccessException {
            return this.mongoDbFactory.getDb(dbName);
        }

        public PersistenceExceptionTranslator getExceptionTranslator() {
            return this.mongoDbFactory.getExceptionTranslator();
        }
    }
}
