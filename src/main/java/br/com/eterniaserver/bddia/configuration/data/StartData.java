package br.com.eterniaserver.bddia.configuration.data;

import br.com.eterniaserver.bddia.domain.user.entities.UserAccount;
import br.com.eterniaserver.bddia.domain.user.enums.BaseRoles;
import br.com.eterniaserver.bddia.domain.user.models.CreateUserRequest;
import br.com.eterniaserver.bddia.domain.user.repositories.UserAccountRepository;
import br.com.eterniaserver.bddia.domain.user.services.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StartData {

    private final UserAccountService userAccountService;
    private final UserAccountRepository userAccountRepository;

    @Bean
    public CommandLineRunner start() {
        return args -> {
            final String adminLogin = "yuri.n.m.d.s@gmail.com";

            if (userAccountRepository.existsByLogin(adminLogin)) {
                return;
            }

            CreateUserRequest request = new CreateUserRequest(
                    adminLogin,
                    "admin",
                    "Admin",
                    "Master",
                    "PORTUGUESE"
            );

            userAccountService.create(request);

            userAccountRepository.findByLogin(adminLogin).ifPresent(user -> {
                user.addRole(BaseRoles.ADMIN);
                userAccountRepository.save(user);
            });
        };
    }

}
