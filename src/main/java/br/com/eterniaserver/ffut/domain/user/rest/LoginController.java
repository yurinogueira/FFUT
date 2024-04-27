package br.com.eterniaserver.ffut.domain.user.rest;

import br.com.eterniaserver.ffut.Constants;
import br.com.eterniaserver.ffut.domain.user.entities.UserAccount;
import br.com.eterniaserver.ffut.domain.user.models.AuthenticateRequest;
import br.com.eterniaserver.ffut.domain.user.models.AuthenticateResponse;
import br.com.eterniaserver.ffut.domain.user.models.VerifyTokenRequest;
import br.com.eterniaserver.ffut.domain.user.models.VerifyTokenResponse;
import br.com.eterniaserver.ffut.domain.user.repositories.UserAccountRepository;
import br.com.eterniaserver.ffut.domain.user.services.JWTService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/login/")
@RequiredArgsConstructor
public class LoginController {

    private final JWTService jwtService;
    private final UserAccountRepository userAccountRepository;

    @GetMapping("verify/{token}/")
    @ResponseStatus(HttpStatus.OK)
    public void verify(@PathVariable String token) {
        if (jwtService.isValidToken(token)) {
            UserAccount userAccount = userAccountRepository
                    .findByLogin(jwtService.getUserLogin(token))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.USER_NOT_FOUND));

            userAccount.setVerified(true);

            userAccountRepository.save(userAccount);

            return;
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constants.INVALID_TOKEN);
    }
    @PostMapping("check/")
    @ResponseStatus(HttpStatus.OK)
    public VerifyTokenResponse verify(@RequestBody @Valid VerifyTokenRequest request) {
        return jwtService.verify(request);
    }

    @PostMapping("auth/")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticateResponse authenticate(@RequestBody @Valid AuthenticateRequest request) {
        UserAccount userAccount = userAccountRepository
                .findByLogin(request.getLogin())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.USER_NOT_FOUND));

        request.setUserAccount(userAccount);

        return jwtService.authenticate(request);
    }

}
