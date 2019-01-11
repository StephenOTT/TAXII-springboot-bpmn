package io.digitalstate.taxii.model.status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.stix.bundle.BundleableObject;
import io.digitalstate.taxii.common.json.views.AdminView;
import io.digitalstate.taxii.common.json.views.TaxiiSpecView;
import io.digitalstate.taxii.model.TaxiiModel;
import org.immutables.serial.Serial;
import org.immutables.value.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Value.Immutable @Serial.Version(1L)
@Value.Style(typeImmutable = "TaxiiStatus")
@JsonSerialize(as = TaxiiStatus.class) @JsonDeserialize(builder = TaxiiStatus.Builder.class)
@JsonPropertyOrder({"id", "status", "request_timestamp", "total_count", "success_count", "successes", "failure_count",
        "failures", "failure_count", "pending_count", "pendings" })
public interface TaxiiStatusResource extends TaxiiModel {

    @NotBlank
    @JsonProperty("id")
    @Value.Default
    @JsonView({TaxiiSpecView.class, AdminView.class})
    default String getId(){
        return UUID.randomUUID().toString();
    }

    @NotBlank
    @JsonProperty("status")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    String getStatus(); // Add Vocab restriction

    @JsonProperty("request_timestamp") @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Optional<Instant> getRequestTimestamp();

    @NotNull @Min(0)
    @JsonProperty("total_count")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Long getTotalCount();

    @NotNull @Min(0)
    @JsonProperty("success_count")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Long getSuccessCount();

    @NotNull
    @JsonProperty("successes")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Set<String> getSuccesses();

    @NotNull @Min(0)
    @JsonProperty("failure_count")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Long getFailureCount();

    @NotNull
    @JsonProperty("failures")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Set<TaxiiStatusFailureResource> getFailures();

    @NotNull @Min(0)
    @JsonProperty("pending_count")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Long getPendingCount();

    @NotNull
    @JsonProperty("pendings")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Set<BundleableObject> getPendings();

}
