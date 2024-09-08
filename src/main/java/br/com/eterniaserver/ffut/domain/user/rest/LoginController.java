package br.com.eterniaserver.ffut.domain.user.rest;

import br.com.eterniaserver.ffut.Constants;
import br.com.eterniaserver.ffut.domain.user.entities.UserAccountEntity;
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
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/login/")
@RequiredArgsConstructor
public class LoginController {

    private final JWTService jwtService;
    private final UserAccountRepository userAccountRepository;

    @GetMapping("verify/{token}/")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView verify(@PathVariable String token) {
        ModelAndView modelAndView = new ModelAndView();

        if (jwtService.isValidToken(token)) {
            UserAccountEntity userAccountEntity = userAccountRepository
                    .findByLogin(jwtService.getUserLogin(token))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.USER_NOT_FOUND));

            userAccountEntity.setVerified(true);

            userAccountRepository.save(userAccountEntity);

            modelAndView.setViewName("validated-token.html");
            return modelAndView;
        }

        modelAndView.setViewName("invalid-token.html");
        return modelAndView;
    }
    @PostMapping("check/")
    @ResponseStatus(HttpStatus.OK)
    public VerifyTokenResponse verify(@RequestBody @Valid VerifyTokenRequest request) {
        return jwtService.verify(request);
    }

    @PostMapping("auth/")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticateResponse authenticate(@RequestBody @Valid AuthenticateRequest request) {
        UserAccountEntity userAccountEntity = userAccountRepository
                .findByLogin(request.getLogin())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.USER_NOT_FOUND));

        request.setUserAccountEntity(userAccountEntity);

        return jwtService.authenticate(request);
    }

}
