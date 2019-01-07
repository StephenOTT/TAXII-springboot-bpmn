package io.digitalstate.taxii.mongo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.stix.bundle.BundleObject;
import io.digitalstate.taxii.common.TaxiiParsers;
import org.immutables.value.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.annotation.Id;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Value.Immutable
@JsonSerialize(as=ImmutableCollectionObjectDocument.class) @JsonDeserialize(builder = ImmutableCollectionObjectDocument.Builder.class)
@Document(collection = "objects")
@JsonTypeName("collection_object")
public interface CollectionObjectDocument extends TaxiiMongoModel {

    @Override
    @Value.Default
    default String type() {
        return "collection_object";
    }

    @JsonProperty("tenant_id")
    String tenantId();

    @JsonProperty("collection_id")
    String collectionId();

    @JsonProperty("bundle")
    BundleObject bundle();


    @WritingConverter
    public class MongoWriterConverter implements Converter<CollectionObjectDocument, org.bson.Document> {
        public org.bson.Document convert(final CollectionObjectDocument object) {
            org.bson.Document doc = org.bson.Document.parse(object.toMongoJson());
            return doc;
        }
    }

    @ReadingConverter
    public class MongoReaderConverter implements Converter<org.bson.Document, CollectionObjectDocument> {
        public CollectionObjectDocument convert(final org.bson.Document object) {
            try {
                return TaxiiParsers.getJsonMapper().readValue(object.toJson(), CollectionObjectDocument.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
