package br.com.eterniaserver.ffut.domain.challenge.rest;

import br.com.eterniaserver.ffut.domain.challenge.models.*;
import br.com.eterniaserver.ffut.domain.challenge.services.ChallengeAnswerService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answer/")
@RequiredArgsConstructor
public class ChallengeAnswerController {

    private final ChallengeAnswerService service;

    @GetMapping("{challengeId}/{userId}/")
    @ResponseStatus(HttpStatus.OK)
    public ListChallengeAnswerResponse list(@PathVariable String challengeId, @PathVariable String userId) {
        ListChallengeAnswerRequest request = new ListChallengeAnswerRequest(challengeId, userId);
        return service.list(request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateChallengeAnswerResponse create(@RequestBody @Valid CreateChallengeAnswerRequest request) {
        return service.create(request);
    }

    @GetMapping("{answerId}/")
    @ResponseStatus(HttpStatus.OK)
    public ReadChallengeAnswerResponse read(@PathVariable String answerId) {
        return service.read(answerId);
    }
}
