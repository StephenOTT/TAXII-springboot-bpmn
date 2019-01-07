package io.digitalstate.taxii.mongo.model.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.taxii.common.TaxiiParsers;
import io.digitalstate.taxii.model.apiroot.TaxiiApiRootResource;
import io.digitalstate.taxii.mongo.model.TaxiiMongoModel;
import org.immutables.value.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;

@Value.Immutable
@Value.Style(passAnnotations = {Document.class, CompoundIndexes.class})
@JsonSerialize(as=ImmutableTenantDocument.class) @JsonDeserialize(builder = ImmutableTenantDocument.Builder.class)
@Document(collection = "tenants")
@JsonTypeName("tenant")
@CompoundIndexes({
        @CompoundIndex(name = "tenant_id", def = "{ 'tenant.tenant_id': 1 }", unique = true),
        @CompoundIndex(name = "tenant_slug", def = "{ 'tenant.tenant_slug': 1 }", unique = true)
})
@JsonPropertyOrder({"_id", "type", "created_at", "modified_at", "tenant" })
public interface TenantDocument extends TaxiiMongoModel {

    @Override
    @Value.Default
    default String type() {
        return "tenant";
    }

    @JsonProperty("tenant")
    TaxiiApiRootResource tenant();


    @WritingConverter
    public class MongoWriterConverter implements Converter<TenantDocument, org.bson.Document> {
        public org.bson.Document convert(final TenantDocument object) {
            org.bson.Document doc = org.bson.Document.parse(object.toMongoJson());
            return doc;
        }
    }

    @ReadingConverter
    public class MongoReaderConverter implements Converter<org.bson.Document, TenantDocument> {
        public TenantDocument convert(final org.bson.Document object) {
            try {
                return TaxiiParsers.getMongoMapper().readValue(object.toJson(), TenantDocument.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
