package br.com.eterniaserver.ffut.domain.challenge.repositories;

import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeAnswerEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeAnswerRepository extends MongoRepository<ChallengeAnswerEntity, String> {

    List<ChallengeAnswerEntity> findAllByUserId(Pageable pageable, String userId);

    List<ChallengeAnswerEntity> findAllByChallengeIdAndUserId(String challengeId, String userId);

}
