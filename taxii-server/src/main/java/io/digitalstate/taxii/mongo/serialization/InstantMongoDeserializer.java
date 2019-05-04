package io.digitalstate.taxii.mongo.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import io.digitalstate.stix.helpers.StixDataFormats;
import io.digitalstate.stix.sdo.objects.AttackPatternSdo;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class InstantMongoDeserializer extends StdDeserializer<Instant> {

    private DateTimeFormatter formatter = StixDataFormats.getReaderStixDateTimeFormatter();

    public InstantMongoDeserializer() {
        super(Instant.class);
    }

    @Override
    public Instant deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = jp.readValueAsTree();

        if(node.isValueNode()){
             TemporalAccessor ta = formatter.parse(node.asText());
             return Instant.from(ta);
        } else {
            long dateNumber = node.get("$date").traverse().getLongValue();
            return Instant.ofEpochMilli(dateNumber);
        }

    }
}