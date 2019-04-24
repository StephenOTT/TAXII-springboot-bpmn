package io.digitalstate.taxii.mongo.repository.impl.collection;

import io.digitalstate.taxii.mongo.model.document.CollectionMembershipDocument;

import javax.validation.constraints.NotNull;

public interface CollectionMembershipsRepositoryCustom {

    CollectionMembershipDocument createCollectionMembership(@NotNull CollectionMembershipDocument collectionMembershipDocument, @NotNull String targetTenantId);
}
