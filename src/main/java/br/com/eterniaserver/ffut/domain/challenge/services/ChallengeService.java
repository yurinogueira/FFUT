package br.com.eterniaserver.ffut.domain.challenge.services;

import br.com.eterniaserver.ffut.Constants;
import br.com.eterniaserver.ffut.domain.challenge.dtos.ChallengeDto;
import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeEntity;
import br.com.eterniaserver.ffut.domain.challenge.repositories.ChallengeRepository;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    @Transactional
    public List<ChallengeDto> list(int page, int size) {
        return challengeRepository
                .findAll(PageRequest.of(page, size))
                .stream()
                .map(entity -> new ChallengeDto(entity.getName(), entity.getDescription(), entity.getCode()))
                .toList();
    }

    @Transactional
    public ChallengeEntity create(ChallengeDto data) {
        ChallengeEntity entity = new ChallengeEntity();

        entity.setName(data.name());
        entity.setDescription(data.description());
        entity.setCode(data.code());
        entity.setChallengeVersion(0);

        challengeRepository.save(entity);

        return entity;
    }

    @Transactional
    public ChallengeDto read(String id) {
        ChallengeEntity entity = getById(id);

        return new ChallengeDto(entity.getName(), entity.getDescription(), entity.getCode());
    }

    @Transactional
    public void update(String id, ChallengeDto data) {
        ChallengeEntity entity = getById(id);

        entity.setName(data.name());
        entity.setDescription(data.description());
        entity.setCode(data.code());

        entity.incrementChallengeVersion();

        challengeRepository.save(entity);
    }

    @Transactional
    public void delete(String id) {
        ChallengeEntity entity = getById(id);

        challengeRepository.delete(entity);
    }

    private ChallengeEntity getById(String id) {
        return challengeRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.CHALLENGE_NOT_FOUND));
    }
}
