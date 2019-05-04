package io.digitalstate.taxii.mongo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.digitalstate.taxii.common.TaxiiParsers;
import org.immutables.value.Value;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Value.Style(additionalJsonAnnotations = {JsonTypeName.class})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
public interface TaxiiMongoModel extends Serializable {

    @JsonProperty("type")
    String type();

    @Value.Default
    @JsonProperty("_id")
    @Id
    default String id() {
        return UUID.randomUUID().toString();
    }

    @Value.Default
    @JsonProperty("created_at")
    default Instant createdAt() {
        return Instant.now();
    }

    @Value.Default
    @JsonProperty("modified_at")
    default Instant modifiedAt() {
        return createdAt();
    }

    @Value.Lazy
    default String toJson() {
        try {
            return TaxiiParsers.getJsonMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to parse into json string", e);
        }
    }

    @Value.Lazy
    default String toMongoJson() {
        try {
            return TaxiiParsers.getMongoMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to parse into json string", e);
        }
    }

}
