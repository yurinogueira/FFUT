package br.com.eterniaserver.bddia.domain.user.models;

import br.com.eterniaserver.bddia.domain.user.dtos.TokenDto;

public record AuthenticateResponse(TokenDto tokenDto) { }
