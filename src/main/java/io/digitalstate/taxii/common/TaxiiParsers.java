package io.digitalstate.taxii.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.digitalstate.stix.helpers.StixDataFormats;
import io.digitalstate.stix.json.StixParsers;
import io.digitalstate.taxii.mongo.serialization.InstantMongoDeserializer;
import io.digitalstate.taxii.mongo.serialization.InstantMongoSerializer;
import io.digitalstate.taxii.mongo.model.*;

import java.text.SimpleDateFormat;
import java.time.Instant;

public class TaxiiParsers {

    static ObjectMapper objectMapperMongo = generateMongoMapper();
    static ObjectMapper objectMapperJson = generateJsonMapper();

    public static ObjectMapper getJsonMapper(){
        return objectMapperJson;
    }

    public static ObjectMapper getMongoMapper(){
        return objectMapperMongo;
    }

    public static ObjectMapper generateMongoMapper(){
        ObjectMapper mapper = StixParsers.getJsonMapper(true).copy();
        registerSubTypes(mapper);
        mapper.registerModule(generateMongoInstantModule());
        return mapper;
    }

    public static ObjectMapper generateJsonMapper(){
        ObjectMapper mapper = StixParsers.getJsonMapper(true).copy();
        registerSubTypes(mapper);
        mapper.setDateFormat(new SimpleDateFormat(StixDataFormats.TIMESTAMP_PATTERN));
        return mapper;
    }

    public static void registerSubTypes(ObjectMapper objectMapper){
        objectMapper.registerSubtypes(TenantDocument.class);
        objectMapper.registerSubtypes(UserDocument.class);
        objectMapper.registerSubtypes(StatusDocument.class);
        objectMapper.registerSubtypes(DiscoveryDocument.class);
        objectMapper.registerSubtypes(CollectionDocument.class);
        objectMapper.registerSubtypes(CollectionObjectDocument.class);
    }

    public static SimpleModule generateMongoInstantModule(){
        SimpleModule module = new SimpleModule();
        module.addSerializer(Instant.class, new InstantMongoSerializer());
        module.addDeserializer(Instant.class, new InstantMongoDeserializer());
        return module;
    }
}
