package br.com.eterniaserver.ffut.domain.challenge.models;

import br.com.eterniaserver.ffut.domain.challenge.enums.MutationType;
import lombok.Data;

@Data
public class MutationResultModel {

    private MutationType mutationType;

    private String mutationInfo;

    private Boolean isKilled;

    private Integer line;
}
