package io.digitalstate.taxii.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.digitalstate.stix.common.StixInstant;
import io.digitalstate.stix.json.StixParsers;
import io.digitalstate.taxii.mongo.model.document.*;
import io.digitalstate.taxii.mongo.serialization.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class TaxiiParsers {

    private static final Logger log = LoggerFactory.getLogger(TaxiiParsers.class);

    private static ObjectMapper mongoMapper = StixParsers.generateJsonMapperBase()
            .registerModule(StixParsers.generateStixSubTypesModule())
            .registerModule(generateTaxiiMongoDocSubTypesModule())
            .registerModule(generateMongoStixInstantModule())
            .registerModule(generateMongoInstantModule())
            .registerModule(generateMongoLongModule());

    private static ObjectMapper jsonMapper = StixParsers.getJsonMapper()
            .registerModule(generateTaxiiMongoDocSubTypesModule());


    public static ObjectMapper getJsonMapper(){
        return jsonMapper;
    }

    public static ObjectMapper getMongoMapper(){
        return mongoMapper;
    }

    public static SimpleModule generateTaxiiMongoDocSubTypesModule(){
        SimpleModule module = new SimpleModule();
        module.registerSubtypes(TenantDocument.class,
                UserDocument.class,
                UserRolesDocument.class,
                StatusDocument.class,
                DiscoveryDocument.class,
                CollectionDocument.class,
                CollectionObjectDocument.class,
                CollectionMembershipDocument.class);
        return module;
    }

    public static SimpleModule generateMongoInstantModule(){
        SimpleModule module = new SimpleModule();
        module.addSerializer(Instant.class, new InstantMongoSerializer());
        module.addDeserializer(Instant.class, new InstantMongoDeserializer());
        return module;
    }

    public static SimpleModule generateMongoStixInstantModule(){
        SimpleModule module = new SimpleModule();
        module.addSerializer(StixInstant.class, new StixInstantMongoSerializer());
        module.addDeserializer(StixInstant.class, new StixInstantMongoDeserializer());
        return module;
    }

    public static SimpleModule generateMongoLongModule(){
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, new LongMongoSerializer());
        module.addDeserializer(Long.class, new LongMongoDeserializer());
        return module;
    }
}
