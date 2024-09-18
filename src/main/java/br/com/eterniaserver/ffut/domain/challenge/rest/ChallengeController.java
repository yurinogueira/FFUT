package br.com.eterniaserver.ffut.domain.challenge.rest;

import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeEntity;
import br.com.eterniaserver.ffut.domain.challenge.models.CreateChallengeRequest;
import br.com.eterniaserver.ffut.domain.challenge.models.CreateChallengeResponse;
import br.com.eterniaserver.ffut.domain.challenge.services.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenge/")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService service;

    public CreateChallengeResponse create(@RequestBody @Valid CreateChallengeRequest request) {
        ChallengeEntity entity = service.create(request.toDto());

        return new CreateChallengeResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getChallengeVersion(),
                entity.getCode()
        );
    }

}
