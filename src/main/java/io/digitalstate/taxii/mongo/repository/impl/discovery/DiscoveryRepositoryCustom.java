package io.digitalstate.taxii.mongo.repository.impl.discovery;

import io.digitalstate.taxii.mongo.model.document.DiscoveryDocument;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Implements with {@link DiscoveryRepositoryImpl} through auto-detection
 */
public interface DiscoveryRepositoryCustom {

    Optional<DiscoveryDocument> findDiscovery();

    DiscoveryDocument createDiscovery(@NotNull DiscoveryDocument discoveryDoc);

}
