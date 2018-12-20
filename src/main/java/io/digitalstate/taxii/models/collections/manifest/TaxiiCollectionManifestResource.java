package io.digitalstate.taxii.models.collections.manifest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Value.Immutable
@Value.Style(typeImmutable = "TaxiiCollectionManifest")
@JsonSerialize(as = TaxiiCollectionManifest.class) @JsonDeserialize(builder = TaxiiCollectionManifest.Builder.class)
public interface TaxiiCollectionManifestResource {

    @NotBlank
    @JsonProperty("objects")
    Set<TaxiiCollectionManifestEntryResource> getObjects();


}
