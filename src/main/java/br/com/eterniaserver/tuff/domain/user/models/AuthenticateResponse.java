package br.com.eterniaserver.tuff.domain.user.models;

import br.com.eterniaserver.tuff.domain.user.dtos.TokenDto;

public record AuthenticateResponse(TokenDto tokenDto) { }
