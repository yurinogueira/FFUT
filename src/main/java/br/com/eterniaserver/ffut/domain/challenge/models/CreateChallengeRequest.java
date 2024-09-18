package br.com.eterniaserver.ffut.domain.challenge.models;

import br.com.eterniaserver.ffut.domain.challenge.dtos.ChallengeDto;

public record CreateChallengeRequest(String name, String description, String code) {

    public ChallengeDto toDto() {
        return new ChallengeDto(name, description, code);
    }

}
