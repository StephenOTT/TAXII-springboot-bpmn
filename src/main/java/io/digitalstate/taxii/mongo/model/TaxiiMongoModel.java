package io.digitalstate.taxii.mongo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.digitalstate.taxii.common.TaxiiParsers;
import org.immutables.value.Value;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Value.Style(additionalJsonAnnotations = {JsonTypeName.class})
public interface TaxiiMongoModel {

    @JsonProperty("type")
    String type();

    @Value.Default
    @JsonProperty("_id") @Id
    default String id(){
        return UUID.randomUUID().toString();
    }

    @Value.Default
    @JsonProperty("created_at")
    default Instant createdAt(){
        return Instant.now();
    }

    @JsonProperty("modified_at")
    Instant modifiedAt();

    @Value.Lazy
    default String toJson(){
        try {
            return TaxiiParsers.getJsonMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to parse into json string", e);
        }
    }

    @Value.Lazy
    default String toMongoJson(){
        try {
            return TaxiiParsers.getMongoMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to parse into json string", e);
        }
    }

}
