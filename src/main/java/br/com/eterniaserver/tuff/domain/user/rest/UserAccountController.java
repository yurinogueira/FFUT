package br.com.eterniaserver.tuff.domain.user.rest;

import br.com.eterniaserver.tuff.domain.user.models.CreateUserRequest;
import br.com.eterniaserver.tuff.domain.user.models.CreateUserResponse;
import br.com.eterniaserver.tuff.domain.user.models.DeleteUserRequest;
import br.com.eterniaserver.tuff.domain.user.models.DeleteUserResponse;
import br.com.eterniaserver.tuff.domain.user.models.ReadUserRequest;
import br.com.eterniaserver.tuff.domain.user.models.ReadUserResponse;
import br.com.eterniaserver.tuff.domain.user.models.UpdateUserRequest;
import br.com.eterniaserver.tuff.domain.user.models.UpdateUserResponse;
import br.com.eterniaserver.tuff.domain.user.services.UserAccountService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponse create(@RequestBody @Valid CreateUserRequest request) {
        return userAccountService.create(request);
    }

    @GetMapping("{login}/")
    @ResponseStatus(HttpStatus.OK)
    public ReadUserResponse read(@PathVariable String login) {
        return userAccountService.read(new ReadUserRequest(login));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UpdateUserResponse update(@RequestBody @Valid UpdateUserRequest request) {
        return userAccountService.update(request);
    }

    @DeleteMapping("{login}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public DeleteUserResponse delete(@PathVariable String login) {
        return userAccountService.delete(new DeleteUserRequest(login));
    }

}
