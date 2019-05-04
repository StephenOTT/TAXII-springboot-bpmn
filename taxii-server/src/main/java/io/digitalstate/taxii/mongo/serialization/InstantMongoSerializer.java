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

public class InstantMongoSerializer extends StdSerializer<Instant> {

    private DateTimeFormatter formatter = StixDataFormats.getWriterStixDateTimeFormatter(3);

    public InstantMongoSerializer() {
        this(null);
    }

    public InstantMongoSerializer(Class<Instant> t) {
        super(t);
    }

    @Override
    public void serialize(
            Instant value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        String stringDate = formatter.format(value);
        try {
            jgen.writeRawValue("ISODate(\"" + stringDate + "\")");
        } catch (IOException e) {
            throw new IllegalStateException("Unable to serialize Instant into Mongo format from: " + stringDate);
        }
    }
}