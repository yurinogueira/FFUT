package br.com.eterniaserver.bddia.domain.user.models;

import br.com.eterniaserver.bddia.Constants;
import jakarta.validation.constraints.NotNull;

public record VerifyTokenRequest(@NotNull(message = Constants.TOKEN_NEEDED) String token) { }
