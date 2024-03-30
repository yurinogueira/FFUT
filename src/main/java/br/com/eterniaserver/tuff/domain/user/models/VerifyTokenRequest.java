package br.com.eterniaserver.tuff.domain.user.models;

import br.com.eterniaserver.tuff.Constants;
import jakarta.validation.constraints.NotNull;

public record VerifyTokenRequest(@NotNull(message = Constants.TOKEN_NEEDED) String token) { }
