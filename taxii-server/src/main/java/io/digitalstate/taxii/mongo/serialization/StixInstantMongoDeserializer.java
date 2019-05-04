package io.digitalstate.taxii.mongo.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.digitalstate.stix.common.StixInstant;

import java.io.IOException;

public class StixInstantMongoDeserializer extends StdDeserializer<StixInstant> {

    public StixInstantMongoDeserializer() {
        super(StixInstant.class);
    }

    @Override
    public StixInstant deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.readValueAsTree();
        String fullDate = node.get("value").asText();

        if(!fullDate.isEmpty()){
             return StixInstant.parse(fullDate);
        } else {
            throw new IllegalStateException("Unable to find value field containing the StixInstant string value");
        }
    }
}