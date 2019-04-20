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
import java.io.IOException;
import java.util.Set;

@Value.Immutable @Serial.Version(1L)
@Value.Style(passAnnotations = {Document.class, CompoundIndexes.class})
@JsonSerialize(as= ImmutableUserRolesDocument.class) @JsonDeserialize(builder = ImmutableUserRolesDocument.Builder.class)
@Document(collection = "user_roles")
@JsonTypeName("user_roles")
@CompoundIndexes({
        @CompoundIndex(name = "tenant_id_username", def = "{ 'tenant_id': 1, 'username':1 }", unique = true),
})
@JsonPropertyOrder({"_id", "type", "created_at", "modified_at", "tenant_id", "username", "roles" })
public interface UserRolesDocument extends TaxiiMongoModel {

    @Override
    @Value.Default
    default String type() {
        return "user_roles";
    }

    @JsonProperty("tenant_id")
    @NotBlank
    String tenantId();

    @JsonProperty("user_id")
    @NotBlank
    String getUserId();

    @JsonProperty("roles")
    @NotBlank
    Set<String> getRoles();


    @WritingConverter
    public class MongoWriterConverter implements Converter<UserRolesDocument, org.bson.Document> {
        public org.bson.Document convert(final UserRolesDocument object) {
            org.bson.Document doc = org.bson.Document.parse(object.toMongoJson());
            return doc;
        }
    }

    @ReadingConverter
    public class MongoReaderConverter implements Converter<org.bson.Document, UserRolesDocument> {
        public UserRolesDocument convert(final org.bson.Document object) {
            try {
                return TaxiiParsers.getMongoMapper().readValue(object.toJson(), UserRolesDocument.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
