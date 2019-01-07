package io.digitalstate.taxii.model.discovery;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.taxii.common.json.views.AdminView;
import io.digitalstate.taxii.common.json.views.TaxiiSpecView;
import io.digitalstate.taxii.model.TaxiiModel;
import org.immutables.value.Value;

import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Value.Immutable
@Value.Style(typeImmutable = "TaxiiDiscovery")
@JsonSerialize(as = TaxiiDiscovery.class) @JsonDeserialize(builder = TaxiiDiscovery.Builder.class)
@JsonPropertyOrder({"title", "description", "contact", "default", "api_roots"})
public interface TaxiiDiscoveryResource extends TaxiiModel {

    @NotBlank
    @JsonProperty("title")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    String getTitle();

    @JsonProperty("description") @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Optional<String> getDescription();

    @JsonProperty("contact") @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Optional<String> getContact();

    @JsonProperty("default") @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Optional<String> getDefaultApiRoot();

    /**
     * This has been left as a Set of Strings rather than a sub object
     * of {@link io.digitalstate.taxii.model.TaxiiApiRoot} for future flexibility reasons.
     * In practice there would be a search for roots and converted into a set of strings and then that set used
     * to populate the set of roots in this field.
     * @return
     */
    @JsonProperty("api_roots") @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Set<String> getApiRoots();

}

