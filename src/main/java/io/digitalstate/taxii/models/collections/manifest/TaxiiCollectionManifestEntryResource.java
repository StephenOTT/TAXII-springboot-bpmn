package io.digitalstate.taxii.models.collections.manifest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import io.digitalstate.stix.helpers.StixDataFormats;
import org.immutables.value.Value;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Value.Immutable
@Value.Style(typeImmutable = "TaxiiCollectionManifestEntry")
@JsonSerialize(as = TaxiiCollectionManifestEntry.class) @JsonDeserialize(builder = TaxiiCollectionManifestEntry.Builder.class)
public interface TaxiiCollectionManifestEntryResource {

    @NotBlank
    @JsonProperty("id")
    String getId();

    @JsonProperty("date_added") @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    @JsonSerialize(using = InstantSerializer.class)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = StixDataFormats.TIMESTAMP_PATTERN, timezone = "UTC")
    Optional<Instant> getDateAdded();

    @JsonProperty("versions") @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    Set<String> getVersions();

    @JsonProperty("media_types") @JsonInclude(value = NON_EMPTY, content= NON_EMPTY)
    Set<String> getMediaTypes();


}
