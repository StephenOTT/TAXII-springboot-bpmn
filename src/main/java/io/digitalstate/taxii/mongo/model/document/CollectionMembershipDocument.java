package io.digitalstate.taxii.mongo.model.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.taxii.common.TaxiiParsers;
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
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Value.Immutable @Serial.Version(1L)
@Value.Style(passAnnotations = {Document.class, CompoundIndexes.class})
@JsonSerialize(as=ImmutableCollectionMembershipDocument.class) @JsonDeserialize(builder = ImmutableCollectionMembershipDocument.Builder.class)
@Document(collection = "collection_memberships")
@JsonTypeName("collection_membership")
@CompoundIndexes({
        @CompoundIndex(name = "tenant_username_collection",
                def = "{ 'tenant_id': 1, 'user_id': 1, 'collection_id': 1 }",
                unique = true)
})
@JsonPropertyOrder({"_id", "type", "created_at", "modified_at", "tenant_id", "user_id", "collection_id", "can_read", "can_write" })
public interface CollectionMembershipDocument extends TaxiiMongoModel {

    @Override
    @Value.Default
    default String type() {
        return "collection_membership";
    }

    @JsonProperty("tenant_id")
    @NotBlank
    String tenantId();

    @JsonProperty("used_id")
    @NotBlank
    String getUserId();

    @JsonProperty("collection_id")
    @NotBlank
    String getCollectionId();

    @JsonProperty("can_read")
    @NotNull
    boolean canRead();

    @JsonProperty("can_write")
    @NotNull
    boolean canWrite();



    @WritingConverter
    public class MongoWriterConverter implements Converter<CollectionMembershipDocument, org.bson.Document> {
        public org.bson.Document convert(final CollectionMembershipDocument object) {
            org.bson.Document doc = org.bson.Document.parse(object.toMongoJson());
            return doc;
        }
    }

    @ReadingConverter
    public class MongoReaderConverter implements Converter<org.bson.Document, CollectionMembershipDocument> {
        public CollectionMembershipDocument convert(final org.bson.Document object) {
            try {
                return TaxiiParsers.getMongoMapper().readValue(object.toJson(), CollectionMembershipDocument.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
