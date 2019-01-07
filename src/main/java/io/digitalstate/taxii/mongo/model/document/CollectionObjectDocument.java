package io.digitalstate.taxii.mongo.model.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.stix.bundle.BundleObject;
import io.digitalstate.stix.bundle.BundleableObject;
import io.digitalstate.taxii.common.TaxiiParsers;
import io.digitalstate.taxii.mongo.model.TaxiiMongoModel;
import org.immutables.value.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;

@Value.Immutable
@Value.Style(passAnnotations = {Document.class, CompoundIndexes.class})
@JsonSerialize(as=ImmutableCollectionObjectDocument.class) @JsonDeserialize(builder = ImmutableCollectionObjectDocument.Builder.class)
@Document(collection = "objects")
@JsonTypeName("collection_object")
@JsonPropertyOrder({"_id", "type", "tenant_id", "created_at", "modified_at", "collection_id", "objects" })
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

    @JsonProperty("objects")
    BundleableObject bundle();


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
                return TaxiiParsers.getMongoMapper().readValue(object.toJson(), CollectionObjectDocument.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
