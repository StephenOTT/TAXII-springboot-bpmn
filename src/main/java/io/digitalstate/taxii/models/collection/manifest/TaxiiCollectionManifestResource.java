package io.digitalstate.taxii.models.collection.manifest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digitalstate.taxii.common.json.views.AdminView;
import io.digitalstate.taxii.common.json.views.TaxiiSpecView;
import io.digitalstate.taxii.models.TaxiiModel;
import org.immutables.value.Value;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Value.Immutable
@Value.Style(typeImmutable = "TaxiiCollectionManifest")
@JsonSerialize(as = TaxiiCollectionManifest.class) @JsonDeserialize(builder = TaxiiCollectionManifest.Builder.class)
public interface TaxiiCollectionManifestResource extends TaxiiModel {

    @NotBlank
    @JsonProperty("objects")
    @JsonView({TaxiiSpecView.class, AdminView.class})
    Set<TaxiiCollectionManifestEntryResource> getObjects();


}
