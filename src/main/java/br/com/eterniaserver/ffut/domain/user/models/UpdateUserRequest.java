package br.com.eterniaserver.ffut.domain.user.models;

import br.com.eterniaserver.ffut.Constants;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
        @NotNull(message = Constants.LOGIN_NEEDED) String login,
        @NotNull(message = Constants.NAME_NEEDED) String name,
        @NotNull(message = Constants.SURNAME_NEEDED) String surname
) { }
