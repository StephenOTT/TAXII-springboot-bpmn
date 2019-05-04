package io.digitalstate.taxii.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.digitalstate.taxii.common.TaxiiParsers;
import org.immutables.value.Value;

import java.io.Serializable;

public interface TaxiiModel extends Serializable {

    @Value.Lazy
    default String toJson() {
        try {
            return TaxiiParsers.getJsonMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to parse into json string", e);
        }
    }
}
