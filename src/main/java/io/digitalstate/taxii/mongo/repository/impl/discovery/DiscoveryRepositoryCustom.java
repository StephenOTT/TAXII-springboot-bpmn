package io.digitalstate.taxii.mongo.repository.impl.discovery;

import io.digitalstate.taxii.mongo.model.document.DiscoveryDocument;

import java.util.Optional;

public interface DiscoveryRepositoryCustom {

    Optional<DiscoveryDocument> findDiscovery();

}
