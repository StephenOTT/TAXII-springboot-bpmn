package io.digitalstate.taxii.mongo.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;

public class InstantMongoDeserializer extends StdDeserializer<Instant> {

    public InstantMongoDeserializer() {
        super(Instant.class);
    }

    @Override
    public Instant deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        long dateNumber = jp.readValueAsTree().get("$date").traverse().getLongValue();

        return Instant.ofEpochMilli(dateNumber);
    }
}