package br.com.eterniaserver.ffut.domain.challenge.models;

import lombok.Data;

@Data
public class ChallengeRank {

    private String userId;

    private String username;

    private ChallengeResultModel challengeResultModel;
}
