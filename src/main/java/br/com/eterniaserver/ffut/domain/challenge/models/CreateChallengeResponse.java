package br.com.eterniaserver.ffut.domain.challenge.models;

public record CreateChallengeResponse(String id, String name, String description, Integer challengeVersion, String code) {
}
