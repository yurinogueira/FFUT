package br.com.eterniaserver.ffut.domain.user.dtos;

public record TokenDto(String login,
                       String token,
                       String[] roles) { }
