package io.digitalstate.taxii.mongo.model.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.taxii.common.TaxiiParsers;
import io.digitalstate.taxii.model.status.TaxiiStatusResource;
import io.digitalstate.taxii.mongo.model.TaxiiMongoModel;
import org.immutables.serial.Serial;
import org.immutables.value.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.io.IOException;

@Value.Immutable @Serial.Version(1L)
@Value.Style(passAnnotations = {Document.class, CompoundIndexes.class})
@JsonSerialize(as=ImmutableStatusDocument.class) @JsonDeserialize(builder = ImmutableStatusDocument.Builder.class)
@Document(collection = "statuses")
@JsonTypeName("status")
@JsonPropertyOrder({"_id", "type", "tenant_id", "created_at", "modified_at", "process_instance_id", "last_reported_status" })
@CompoundIndexes({
        @CompoundIndex(name = "tenant_id", def = "{ 'tenant_id': 1 }"),
        @CompoundIndex(name = "process_instance_id", def = "{ 'process_instance_id': 1 }", unique = true)
})
public interface StatusDocument extends TaxiiMongoModel {

    @Override
    @Value.Default
    default String type() {
        return "status";
    }

    @JsonProperty("tenant_id")
    @NotBlank
    String tenantId();

    @JsonProperty("collection_id")
    String collectionId();

    @JsonProperty("process_instance_id")
    String processInstanceId();

    @JsonProperty("last_reported_status")
    String lastReportedStatus();

    @JsonProperty("status_resource")
    TaxiiStatusResource statusResource();


    @WritingConverter
    public class MongoWriterConverter implements Converter<StatusDocument, org.bson.Document> {
        public org.bson.Document convert(final StatusDocument object) {
            org.bson.Document doc = org.bson.Document.parse(object.toMongoJson());
            return doc;
        }
    }

    @ReadingConverter
    public class MongoReaderConverter implements Converter<org.bson.Document, StatusDocument> {
        public StatusDocument convert(final org.bson.Document object) {
            try {
                return TaxiiParsers.getMongoMapper().readValue(object.toJson(), StatusDocument.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
