package io.digitalstate.taxii.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import io.digitalstate.taxii.mongo.model.document.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMongoRepositories
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();

        converters.add(new UserDocument.MongoReaderConverter());
        converters.add(new UserDocument.MongoWriterConverter());
        converters.add(new UserRolesDocument.MongoReaderConverter());
        converters.add(new UserRolesDocument.MongoWriterConverter());
        converters.add(new TenantDocument.MongoReaderConverter());
        converters.add(new TenantDocument.MongoWriterConverter());
        converters.add(new StatusDocument.MongoReaderConverter());
        converters.add(new StatusDocument.MongoWriterConverter());
        converters.add(new DiscoveryDocument.MongoReaderConverter());
        converters.add(new DiscoveryDocument.MongoWriterConverter());
        converters.add(new CollectionObjectDocument.MongoReaderConverter());
        converters.add(new CollectionObjectDocument.MongoWriterConverter());
        converters.add(new CollectionDocument.MongoReaderConverter());
        converters.add(new CollectionDocument.MongoWriterConverter());
        converters.add(new CollectionMembershipDocument.MongoReaderConverter());
        converters.add(new CollectionMembershipDocument.MongoWriterConverter());
        converters.add(new StatusFailureDocument.MongoReaderConverter());
        converters.add(new StatusFailureDocument.MongoWriterConverter());
        return new MongoCustomConversions(converters);
    }

    @Value("${taxi.tenant.db_name_prefix:taxii_}")
    private String DB_NAME_PREFIX;

    @Value("${taxi.tenant.default_db_name:default}")
    private String DEFAULT_DB_NAME;

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new MultiTenantMongoDbFactory(new MongoClient(), DEFAULT_DB_NAME, DB_NAME_PREFIX);
    }

    @Bean
    public MappingMongoConverter mongoConverter() throws Exception {
        MappingMongoConverter mongoConverter = new MappingMongoConverter(
                new DefaultDbRefResolver(mongoDbFactory()),
                new MongoMappingContext());
        mongoConverter.setCustomConversions(customConversions());
        mongoConverter.afterPropertiesSet();
        return mongoConverter;
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory(), mongoConverter());
    }

}