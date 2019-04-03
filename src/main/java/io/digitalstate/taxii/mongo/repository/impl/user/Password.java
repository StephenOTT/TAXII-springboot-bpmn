package io.digitalstate.taxii.mongo.repository.impl.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * Password class for storing raw and encoded passwords
 */
@Value.Immutable(builder = false)
@JsonSerialize(as = ImmutablePassword.class) @JsonDeserialize(as = ImmutablePassword.class)
public abstract class Password {

    @JsonProperty("password")
    @Value.Parameter
    public abstract String password();

    /**
     * Indicates if the password is encoded
     * @return
     */
    @JsonProperty("is_encoded")
    @Value.Parameter
    public abstract boolean isEncoded();
}
