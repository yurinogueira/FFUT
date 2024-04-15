package br.com.eterniaserver.ffut.domain.user.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record TokenDto(@NotNull String login,
                       @NotNull String token,
                       @NotNull String[] roles,
                       @NotNull Date expiration) { }
