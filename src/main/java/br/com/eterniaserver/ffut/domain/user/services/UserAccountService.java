package br.com.eterniaserver.ffut.domain.user.services;

import br.com.eterniaserver.ffut.Constants;
import br.com.eterniaserver.ffut.domain.user.dtos.UserDto;
import br.com.eterniaserver.ffut.domain.user.entities.UserAccount;
import br.com.eterniaserver.ffut.domain.user.enums.BaseRoles;
import br.com.eterniaserver.ffut.domain.user.models.CreateUserRequest;
import br.com.eterniaserver.ffut.domain.user.models.CreateUserResponse;
import br.com.eterniaserver.ffut.domain.user.models.DeleteUserRequest;
import br.com.eterniaserver.ffut.domain.user.models.DeleteUserResponse;
import br.com.eterniaserver.ffut.domain.user.models.ReadUserRequest;
import br.com.eterniaserver.ffut.domain.user.models.ReadUserResponse;
import br.com.eterniaserver.ffut.domain.user.models.UpdateUserRequest;
import br.com.eterniaserver.ffut.domain.user.models.UpdateUserResponse;
import br.com.eterniaserver.ffut.domain.user.repositories.UserAccountRepository;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserAccountService {

    private final PasswordEncoder encoder;
    private final UserAccountRepository userAccountRepository;

    @Transactional
    public CreateUserResponse create(CreateUserRequest request) {
        if (userAccountRepository.existsByLogin(request.login())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, Constants.LOGIN_IN_USE);
        }

        UserAccount userAccount = new UserAccount();

        userAccount.setLogin(request.login());
        userAccount.setPassword(encoder.encode(request.password()));
        userAccount.setName(request.name());
        userAccount.setSurname(request.surname());
        userAccount.setLocale(request.locale());
        userAccount.addRole(BaseRoles.USER);
        userAccount.setCreatedAt(LocalDateTime.now());

        userAccount.validate();

        userAccount = userAccountRepository.save(userAccount);

        UserDto userDto = userAccount.toDto();

        return new CreateUserResponse(userDto);
    }

    @Transactional(readOnly = true)
    public ReadUserResponse read(ReadUserRequest request) {
        UserAccount userAccount = userAccountRepository
                .findByLogin(request.login())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.USER_NOT_FOUND));

        UserDto userDto = userAccount.toDto();

        return new ReadUserResponse(userDto);
    }

    @Transactional
    public UpdateUserResponse update(UpdateUserRequest request) {
        boolean sameLogin = request.getLogin().equals(request.getUserDetails().getUsername());
        if (!sameLogin && userAccountRepository.existsByLogin(request.getLogin())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, Constants.LOGIN_IN_USE);
        }

        UserAccount userAccount = userAccountRepository
                .findByLogin(request.getUserDetails().getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.USER_NOT_FOUND));

        userAccount.setLogin(request.getLogin());
        userAccount.setName(request.getName());
        userAccount.setSurname(request.getSurname());

        userAccount.validate();

        userAccount = userAccountRepository.save(userAccount);

        UserDto userDto = userAccount.toDto();

        return new UpdateUserResponse(userDto);
    }

    @Transactional
    public DeleteUserResponse delete(DeleteUserRequest request) {
        if (!userAccountRepository.existsByLogin(request.login())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.USER_NOT_FOUND);
        }

        userAccountRepository.deleteByLogin(request.login());

        return new DeleteUserResponse();
    }

    @Transactional(readOnly = true)
    public UserDetails login(String login) {
        UserAccount userAccount = userAccountRepository
                .findByLogin(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.USER_NOT_FOUND));

        return User
                .builder()
                .username(userAccount.getLogin())
                .password(userAccount.getPassword())
                .roles(userAccount.getRoles().toArray(String[]::new))
                .disabled(false)
                .build();
    }

}
