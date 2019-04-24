package io.digitalstate.taxii.mongo.model.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.taxii.common.TaxiiParsers;
import io.digitalstate.taxii.model.discovery.TaxiiDiscoveryResource;

import io.digitalstate.taxii.mongo.model.TaxiiMongoModel;
import org.immutables.serial.Serial;
import org.immutables.value.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;

@Value.Immutable @Serial.Version(1L)
@Value.Style(passAnnotations = {Document.class, CompoundIndexes.class})
@JsonSerialize(as=ImmutableDiscoveryDocument.class) @JsonDeserialize(builder = ImmutableDiscoveryDocument.Builder.class)
@Document(collection = "discovery")
@JsonTypeName("discovery")
@JsonPropertyOrder({"_id", "type", "created_at", "modified_at", "server_info" })
public interface DiscoveryDocument extends TaxiiMongoModel {

    @Override
    @Value.Default
    default String type() {
        return "discovery";
    }

    @JsonProperty("server_info")
    TaxiiDiscoveryResource serverInfo();

    @WritingConverter
    public class MongoWriterConverter implements Converter<DiscoveryDocument, org.bson.Document> {
        public org.bson.Document convert(final DiscoveryDocument object) {
            org.bson.Document doc = org.bson.Document.parse(object.toMongoJson());
            return doc;
        }
    }

    @ReadingConverter
    public class MongoReaderConverter implements Converter<org.bson.Document, DiscoveryDocument> {
        public DiscoveryDocument convert(final org.bson.Document object) {
            try {
                return TaxiiParsers.getMongoMapper().readValue(object.toJson(), DiscoveryDocument.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
