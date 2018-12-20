package io.digitalstate.taxii.models.apiroot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Value.Immutable
@Value.Style(typeImmutable = "TaxiiApiRoot")
@JsonSerialize(as = TaxiiApiRoot.class) @JsonDeserialize(builder = TaxiiApiRoot.Builder.class)
public interface TaxiiApiRootResource {
    // Consider adding a "default" marker to mark this root as the default

    @NotBlank
    @JsonProperty("url")
    String getUrl(); // Add regex limitations (configurable) on this field for end use generation to ensure only proper namespaces are generated.

    @NotBlank
    @JsonProperty("title")
    String getTitle();

    @JsonProperty("description") @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    Optional<String> getDescription();

    @Size(min = 1)
    @JsonProperty("versions") @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    Set<String> getVersions(); // Add Vocab for Spec versions.  Must have at least v2.0

    /**
     * The maximum size of the request body in octets (8-bit bytes) that the server can support.
     * This applies to requests only and is determined by the server. Requests with total body
     * length values smaller than this value MUST NOT result in an HTTP 413 (Request Entity Too
     * Large) response.
     * @return the max content length of a request body in octets (8-bit bytes)
     */
    @NotNull @Min(0)
    @JsonProperty("max_content_length")
    long getMaxContentLength();

}
