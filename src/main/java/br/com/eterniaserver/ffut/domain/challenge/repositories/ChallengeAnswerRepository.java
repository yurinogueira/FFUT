package br.com.eterniaserver.ffut.domain.challenge.repositories;

import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeAnswerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeAnswerRepository extends MongoRepository<ChallengeAnswerEntity, String> {
}
