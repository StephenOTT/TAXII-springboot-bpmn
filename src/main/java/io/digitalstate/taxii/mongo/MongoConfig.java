package io.digitalstate.taxii.mongo;

import io.digitalstate.taxii.mongo.model.document.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
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

        return new MongoCustomConversions(converters);
    }

}