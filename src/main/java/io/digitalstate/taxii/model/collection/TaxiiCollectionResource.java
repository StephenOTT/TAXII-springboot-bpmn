package io.digitalstate.taxii.model.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.taxii.common.json.views.AdminView;
import io.digitalstate.taxii.common.json.views.TaxiiSpecView;
import io.digitalstate.taxii.model.TaxiiModel;
import org.immutables.value.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Value.Immutable
@Value.Style(typeImmutable = "TaxiiCollection")
@JsonSerialize(as = TaxiiCollection.class) @JsonDeserialize(builder = TaxiiCollection.Builder.class)
public interface TaxiiCollectionResource extends TaxiiModel {

    @NotBlank
    @JsonProperty("id")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    String getId();

    @NotBlank
    @JsonProperty("title")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    String getTitle();

    @JsonProperty("description") @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Optional<String> getDescription();

    @NotNull
    @JsonProperty("can_read")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    boolean getCanRead();

    @NotNull
    @JsonProperty("can_write")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    boolean getCanWrite();

    @NotNull
    @Value.Default
    @JsonProperty("media_types")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    default Set<String> getMediaTypes(){
      Set<String> mediaTypes = new HashSet<>();
      mediaTypes.add("application/vnd.oasis.stix+json");
      return mediaTypes;
    }

}
