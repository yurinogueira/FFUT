package br.com.eterniaserver.bddia.domain.user.models;

import br.com.eterniaserver.bddia.Constants;
import br.com.eterniaserver.bddia.domain.user.entities.UserAccount;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthenticateRequest {

    @NotNull(message = Constants.LOGIN_NEEDED)
    private final String login;

    @NotNull(message = Constants.PASSWORD_NEEDED)
    private final String password;

    @JsonIgnore
    private UserAccount userAccount;

    private AuthenticateRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

}
