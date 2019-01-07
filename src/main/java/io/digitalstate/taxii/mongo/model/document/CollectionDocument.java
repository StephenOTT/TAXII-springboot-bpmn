package io.digitalstate.taxii.mongo.model.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.taxii.common.TaxiiParsers;
import io.digitalstate.taxii.model.collection.TaxiiCollectionResource;
import io.digitalstate.taxii.mongo.model.TaxiiMongoModel;
import org.immutables.value.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;

@Value.Immutable
@JsonSerialize(as=ImmutableCollectionDocument.class) @JsonDeserialize(builder = ImmutableCollectionDocument.Builder.class)
@Document(collection = "collections")
@JsonTypeName("collection")
public interface CollectionDocument extends TaxiiMongoModel {

    @Override
    @Value.Default
    default String type() {
        return "collection";
    }

    @JsonProperty("tenant_id")
    String tenantId();

    TaxiiCollectionResource collection();


    @WritingConverter
    public class MongoWriterConverter implements Converter<CollectionDocument, org.bson.Document> {
        public org.bson.Document convert(final CollectionDocument object) {
            org.bson.Document doc = org.bson.Document.parse(object.toMongoJson());
            return doc;
        }
    }

    @ReadingConverter
    public class MongoReaderConverter implements Converter<org.bson.Document, CollectionDocument> {
        public CollectionDocument convert(final org.bson.Document object) {
            try {
                return TaxiiParsers.getJsonMapper().readValue(object.toJson(), CollectionDocument.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
