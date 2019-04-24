package io.digitalstate.taxii.mongo.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.digitalstate.stix.helpers.StixDataFormats;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LongMongoSerializer extends StdSerializer<Long> {

    public LongMongoSerializer() {
        this(null);
    }

    public LongMongoSerializer(Class<Long> t) {
        super(t);
    }

    @Override
    public void serialize(
            Long value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        try {
            jgen.writeRawValue("NumberLong(" + value + ")");
        } catch (IOException e) {
            throw new IllegalStateException("Unable to serialize Long into Mongo format from: " + value);
        }
    }
}