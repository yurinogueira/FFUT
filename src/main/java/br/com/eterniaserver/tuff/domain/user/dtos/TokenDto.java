package br.com.eterniaserver.tuff.domain.user.dtos;

public record TokenDto(String login,
                       String token,
                       String[] roles) { }
