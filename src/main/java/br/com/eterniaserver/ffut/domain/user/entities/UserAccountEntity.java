package br.com.eterniaserver.ffut.domain.user.entities;

import br.com.eterniaserver.ffut.Constants;
import br.com.eterniaserver.ffut.domain.user.dtos.UserDto;
import br.com.eterniaserver.ffut.domain.user.enums.BaseLocales;
import br.com.eterniaserver.ffut.domain.user.enums.BaseRoles;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Data
@Document(collection = "users")
public class UserAccountEntity {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,8}$");

    @Id
    private String id;

    @Indexed(unique = true)
    private String login;

    private String password;

    private String name;

    private String surname;

    private String locale;

    private LocalDateTime createdAt;

    private Boolean verified = false;

    private Boolean active = true;

    private List<String> roles = new ArrayList<>();

    public String getUsername() {
        return name + " " + surname;
    }

    public void validate() {
        if (roles.stream().anyMatch(role -> BaseRoles.fromString(role).isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.INVALID_ROLE);
        }

        if (BaseLocales.fromString(locale).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.INVALID_LOCALE);
        }

        if (!EMAIL_REGEX.matcher(login).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.INVALID_LOGIN);
        }
    }

    public void addRole(BaseRoles role) {
        roles.add(role.name());
    }

    public UserDto toDto() {
        return new UserDto(
                getLogin(),
                getName(),
                getSurname(),
                getRoles(),
                getCreatedAt()
        );
    }

}
