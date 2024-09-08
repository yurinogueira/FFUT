package br.com.eterniaserver.ffut.domain.challenge.models;

import lombok.Data;

import java.util.List;

@Data
public class ChallengeResultModel {

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

    private List<MutationResultModel> mutationResults;

}
