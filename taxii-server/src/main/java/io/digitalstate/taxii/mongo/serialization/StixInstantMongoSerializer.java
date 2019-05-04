package io.digitalstate.taxii.mongo.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.digitalstate.stix.common.StixInstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StixInstantMongoSerializer extends StdSerializer<StixInstant> {

    private static final Logger logger = LoggerFactory.getLogger(StixInstantMongoSerializer.class);

    public StixInstantMongoSerializer() {
        this(null);
    }

    public StixInstantMongoSerializer(Class<StixInstant> t) {
        super(t);
    }

    @Override
    public void serialize(StixInstant value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        try {
            jgen.writeStartObject();


            jgen.writeFieldName("mongo_date");
            jgen.writeRawValue("ISODate(\"" + value.toString(3) + "\")");

            jgen.writeNumberField("subsecond_precision", value.getOriginalSubSecondPrecisionDigitCount());

            jgen.writeStringField("value", value.toString());


            jgen.writeEndObject();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to serialize StixInstant into Mongo format from: " + value.toString());
        }
    }
}