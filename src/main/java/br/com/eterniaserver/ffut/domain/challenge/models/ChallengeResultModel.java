package br.com.eterniaserver.ffut.domain.challenge.models;

import lombok.Data;

@Data
public class ChallengeResultModel {

    private Double score;

    private Float coverage;

    private Integer killedMutations;

    private Integer existentMutations;

}
