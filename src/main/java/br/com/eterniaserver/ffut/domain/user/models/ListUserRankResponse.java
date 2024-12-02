package br.com.eterniaserver.ffut.domain.user.models;

import java.util.List;

public record ListUserRankResponse(List<UserRankResponse> users, long totalEntries) {

    public record UserRankResponse(String userName, Double score) { }

}

