package br.com.eterniaserver.ffut.domain.challenge.entities;

import br.com.eterniaserver.ffut.domain.challenge.enums.AnswerStatus;
import br.com.eterniaserver.ffut.domain.challenge.enums.MutationType;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;

@Data
@Document(collection = "users")
public class ChallengeAnswerEntity {

    @Id
    private String id;

    private String challengeId;

    private String userId;

    private String username;

    private AnswerStatus status;

    private Integer challengeVersion;

    private String challengeCode;

    private String userTestCode;

    private ChallengeResultEntity challengeResult;

    public Optional<ChallengeResultEntity> getChallengeResult() {
        return Optional.ofNullable(challengeResult);
    }

    @Data
    public static class ChallengeResultEntity {

        private Double score;

        private Integer testsSuccess;

        private Integer testsFailed;

        private Integer testsError;

        private Integer instructionCoverage;

        private Integer instructionMissed;

        private Integer branchCoverage;

        private Integer branchMissed;

        private Integer lineCoverage;

        private Integer lineMissed;

        private Integer complexityCoverage;

        private Integer complexityMissed;

        private Integer methodCoverage;

        private Integer methodMissed;

        private List<MutationResultEntity> mutationResults;

    }

    @Data
    public static class MutationResultEntity {

        private MutationType mutationType;

        private String mutationInfo;

        private Boolean isKilled;

        private Integer line;
    }

}
