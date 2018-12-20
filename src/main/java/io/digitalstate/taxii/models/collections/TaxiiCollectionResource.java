package io.digitalstate.taxii.models.collections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.taxii.models.TaxiiCollection;
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
public interface TaxiiCollectionResource {

    @NotBlank
    @JsonProperty("id")
    String getId();

    @NotBlank
    @JsonProperty("title")
    String getTitle();

    @JsonProperty("description") @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    Optional<String> getDescription();

    @NotNull
    @JsonProperty("can_read")
    boolean getCanRead();

    @NotNull
    @JsonProperty("can_write")
    boolean getCanWrite();

    @NotNull
    @Value.Default
    @JsonProperty("media_types")
    default Set<String> getMediaTypes(){
      Set<String> mediaTypes = new HashSet<>();
      mediaTypes.add("application/vnd.oasis.stix+json");
      return mediaTypes;
    }

}
