package io.digitalstate.taxii.models.status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.stix.bundle.BundleableObject;
import org.immutables.value.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Value.Immutable
@Value.Style(typeImmutable = "TaxiiStatus")
@JsonSerialize(as = TaxiiStatus.class) @JsonDeserialize(builder = TaxiiStatus.Builder.class)
public interface TaxiiStatusResource {

    @NotBlank
    @JsonProperty("id")
    String getId();

    @NotBlank
    @JsonProperty("status")
    String getStatus(); // Add Vocab restriction

    @JsonProperty("request_timestamp") @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    Optional<Instant> getRequestTimestamp();

    @NotNull @Min(0)
    @JsonProperty("total_count")
    long getTotalCount();

    @NotNull @Min(0)
    @JsonProperty("success_count")
    long getSuccessCount();

    @NotNull
    @JsonProperty("successes")
    Set<String> getSuccesses();

    @NotNull @Min(0)
    @JsonProperty("failure_count")
    long getFailureCount();

    @NotNull
    @JsonProperty("failures")
    Set<TaxiiStatusFailureResource> getFailures();

    @NotNull @Min(0)
    @JsonProperty("pending_count")
    long getPendingCount();

    @NotNull
    @JsonProperty("pendings")
    Set<BundleableObject> getPendings();

}
