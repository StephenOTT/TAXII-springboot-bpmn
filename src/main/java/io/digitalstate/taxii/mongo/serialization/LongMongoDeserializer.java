package io.digitalstate.taxii.mongo.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.digitalstate.stix.helpers.StixDataFormats;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class LongMongoDeserializer extends StdDeserializer<Long> {

    public LongMongoDeserializer() {
        super(Long.class);
    }

    @Override
    public Long deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = jp.readValueAsTree();

        if(node.isValueNode()){
             return Long.valueOf(node.asText());
        } else {
            return Long.valueOf(node.get("$numberLong").asText());
        }
    }
}