package br.com.eterniaserver.bddia.domain.user.dtos;

public record TokenDto(String login,
                       String token,
                       String[] roles) { }
