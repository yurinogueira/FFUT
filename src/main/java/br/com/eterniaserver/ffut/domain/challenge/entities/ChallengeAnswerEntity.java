package br.com.eterniaserver.ffut.domain.challenge.entities;

import br.com.eterniaserver.ffut.domain.challenge.models.ChallengeResultModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Optional;

@Data
@Document(collection = "users")
public class ChallengeAnswerEntity {

    @Id
    private String id;

    private String challengeId;

    private String userId;

    private String username;

    private Integer challengeVersion;

    private String challengeCode;

    private String userTestCode;

    private ChallengeResultModel challengeResult;

    public Optional<ChallengeResultModel> getResult() {
        return Optional.ofNullable(challengeResult);
    }

}
