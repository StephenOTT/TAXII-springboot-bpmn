package io.digitalstate.taxii.model.tenant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.taxii.common.json.views.AdminView;
import io.digitalstate.taxii.common.json.views.TaxiiSpecView;
import io.digitalstate.taxii.model.TaxiiModel;
import org.immutables.serial.Serial;
import org.immutables.value.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Value.Immutable @Serial.Version(1L)
@Value.Style(typeImmutable = "TaxiiTenant")
@JsonSerialize(as = TaxiiTenant.class) @JsonDeserialize(builder = TaxiiTenant.Builder.class)
@JsonPropertyOrder({"tenant_id", "tenant_slug", "title", "description", "versions", "max_content_length"})
public interface TaxiiTenantResource extends TaxiiModel {

    @NotBlank
    @JsonProperty("tenant_id")
    @JsonView({AdminView.class})
    String getTenantId();

    @NotBlank
    @JsonProperty("tenant_slug")
    @JsonView({AdminView.class})
    String getTenantSlug(); // Add regex limitations (configurable) on this field for end use generation to ensure only proper namespaces are generated.

    @NotBlank
    @JsonProperty("title")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    String getTitle();

    @JsonProperty("description")
    @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Optional<String> getDescription();

    @Size(min = 1)
    @JsonProperty("versions")
    @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    @JsonView({TaxiiSpecView.class, AdminView.class})
    @Value.Default
    default Set<String> getVersions(){
        return new HashSet<>(Arrays.asList("taxii-2.0"));
    }

    /**
     * The maximum size of the request body in octets (8-bit bytes) that the server can support.
     * This applies to requests only and is determined by the server. Requests with total body
     * length values smaller than this value MUST NOT result in an HTTP 413 (Request Entity Too
     * Large) response.
     * @return the max content length of a request body in octets (8-bit bytes)
     */
    @NotNull @Min(0)
    @JsonProperty("max_content_length")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    @Value.Default
    default long getMaxContentLength(){
        return 10485760; // 10mb
    }

}
