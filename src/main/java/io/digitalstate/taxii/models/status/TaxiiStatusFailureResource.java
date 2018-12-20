package io.digitalstate.taxii.models.status;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.validation.constraints.NotBlank;


@Value.Immutable
@Value.Style(typeImmutable = "TaxiiStatusFailure")
@JsonSerialize(as = TaxiiStatusFailure.class) @JsonDeserialize(builder = TaxiiStatusFailure.Builder.class)
public interface TaxiiStatusFailureResource {

    @NotBlank
    @JsonProperty("id")
    String getId();

    @NotBlank
    @JsonProperty("message")
    String getMessage(); // Add Vocab restriction

}
