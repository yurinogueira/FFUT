package br.com.eterniaserver.ffut.domain.user.models;

import br.com.eterniaserver.ffut.domain.user.dtos.TokenDto;

public record AuthenticateResponse(TokenDto tokenDto) { }
