package br.com.eterniaserver.ffut.domain.challenge.models;

import br.com.eterniaserver.ffut.domain.challenge.enums.AnswerStatus;
import br.com.eterniaserver.ffut.domain.challenge.enums.MutationType;

import java.util.List;
import java.util.Optional;

public record ReadChallengeAnswerResponse(
        String answerId,
        Integer challengeVersion,
        String challengeId,
        String challengeName,
        String userTest,
        AnswerStatus status,
        Optional<ReadChallengeResultResponse> challengeResult
        ) {

    public record ReadChallengeResultResponse(
            Double score,
            Integer testsSuccess,
            Integer testsFailed,
            Integer testsError,
            Integer instructionCoverage,
            Integer instructionMissed,
            Integer branchCoverage,
            Integer branchMissed,
            Integer lineCoverage,
            Integer lineMissed,
            Integer complexityCoverage,
            Integer complexityMissed,
            Integer methodCoverage,
            Integer methodMissed,
            List<ReadMutationResultResponse> mutationResults) {
    }

    public record ReadMutationResultResponse(
            MutationType mutationType,
            String mutationInfo,
            Boolean isKilled,
            Integer line) {
    }

}
