package io.digitalstate.taxii.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.digitalstate.stix.helpers.StixDataFormats;
import io.digitalstate.stix.json.StixParsers;
import io.digitalstate.taxii.mongo.model.document.*;
import io.digitalstate.taxii.mongo.serialization.InstantMongoDeserializer;
import io.digitalstate.taxii.mongo.serialization.InstantMongoSerializer;
import io.digitalstate.taxii.mongo.serialization.LongMongoDeserializer;
import io.digitalstate.taxii.mongo.serialization.LongMongoSerializer;

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
        mapper.registerModule(generateMongoLongModule());
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
        objectMapper.registerSubtypes(UserRolesDocument.class);
        objectMapper.registerSubtypes(StatusDocument.class);
        objectMapper.registerSubtypes(DiscoveryDocument.class);
        objectMapper.registerSubtypes(CollectionDocument.class);
        objectMapper.registerSubtypes(CollectionObjectDocument.class);
        objectMapper.registerSubtypes(CollectionMembershipDocument.class);
    }

    public static SimpleModule generateMongoInstantModule(){
        SimpleModule module = new SimpleModule();
        module.addSerializer(Instant.class, new InstantMongoSerializer());
        module.addDeserializer(Instant.class, new InstantMongoDeserializer());
        return module;
    }

    public static SimpleModule generateMongoLongModule(){
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, new LongMongoSerializer());
        module.addDeserializer(Long.class, new LongMongoDeserializer());
        return module;
    }
}
