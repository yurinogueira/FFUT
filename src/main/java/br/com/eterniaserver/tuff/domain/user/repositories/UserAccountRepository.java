package br.com.eterniaserver.tuff.domain.user.repositories;

import br.com.eterniaserver.tuff.domain.user.entities.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    boolean existsByLogin(String login);

    Optional<UserAccount> findByLogin(String login);

    void deleteByLogin(String login);

    List<UserAccount> findAllByNameContains(String search);

}
