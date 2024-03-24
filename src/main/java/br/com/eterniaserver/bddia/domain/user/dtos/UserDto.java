package br.com.eterniaserver.bddia.domain.user.dtos;

import java.util.List;

public record UserDto(String login,
                      String name,
                      String surname,
                      List<String> roles) { }
