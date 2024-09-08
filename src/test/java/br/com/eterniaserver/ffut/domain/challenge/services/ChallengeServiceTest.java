package br.com.eterniaserver.ffut.domain.challenge.services;

import br.com.eterniaserver.ffut.domain.challenge.dtos.ChallengeDto;
import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeEntity;
import br.com.eterniaserver.ffut.domain.challenge.repositories.ChallengeRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

class ChallengeServiceTest {

    private ChallengeRepository repository;
    private ChallengeService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ChallengeRepository.class);
        service = new ChallengeService(repository);
    }

    @Test
    void testListReturnAll() {
        // Arrange
        ChallengeEntity first = new ChallengeEntity();
        first.setId("#FIRST1");
        first.setName("First");
        first.setDescription("First description");
        first.setCode("First code");

        ChallengeEntity second = new ChallengeEntity();
        second.setId("#SECOND2");
        second.setName("Second");
        second.setDescription("Second description");
        second.setCode("Second code");

        List<ChallengeEntity> challengeEntities = List.of(first, second);

        Pageable pageRequest = Mockito.any(Pageable.class);
        Page<ChallengeEntity> page = new PageImpl<>(challengeEntities);

        Mockito.when(repository.findAll(pageRequest)).thenReturn(page);

        // Act
        List<ChallengeDto> result = service.list(0, 10);

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("First", result.getFirst().name());
        Assertions.assertEquals("First description", result.getFirst().description());
        Assertions.assertEquals("First code", result.getFirst().code());
        Assertions.assertEquals("Second", result.getLast().name());
        Assertions.assertEquals("Second description", result.getLast().description());
        Assertions.assertEquals("Second code", result.getLast().code());
    }

    @Test
    void testCreate() {
        // Arrange
        ChallengeDto challengeDto = new ChallengeDto("First", "First description", "First code");

        ChallengeEntity challengeEntity = new ChallengeEntity();
        challengeEntity.setName("First");
        challengeEntity.setDescription("First description");
        challengeEntity.setCode("First code");
        challengeEntity.setChallengeVersion(0);

        // Act
        service.create(challengeDto);

        // Assert
        Mockito.verify(repository).save(challengeEntity);
    }

    @Test
    void testRead() {
        // Arrange
        ChallengeEntity entity = new ChallengeEntity();
        entity.setId("#FIRST1");
        entity.setName("First");
        entity.setDescription("First description");
        entity.setCode("First code");

        Mockito.when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        // Act
        ChallengeDto result = service.read("#FIRST1");

        // Assert
        Assertions.assertEquals("First", result.name());
        Assertions.assertEquals("First description", result.description());
        Assertions.assertEquals("First code", result.code());
    }

    @Test
    void testReadNotFound() {
        // Arrange
        Mockito.when(repository.findById("#FIRST1")).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(ResponseStatusException.class, () -> service.read("#FIRST1"));
    }

    @Test
    void testUpdate() {
        // Arrange
        ChallengeDto challengeDto = new ChallengeDto("First", "First description", "First code");

        ChallengeEntity entity = new ChallengeEntity();
        entity.setId("#FIRST1");
        entity.setName("First");
        entity.setDescription("First description");
        entity.setCode("First code");
        entity.setChallengeVersion(0);

        Mockito.when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        // Act
        service.update("#FIRST1", challengeDto);

        // Assert
        Mockito.verify(repository).save(entity);
    }

    @Test
    void testUpdateNotFound() {
        // Arrange
        ChallengeDto challengeDto = new ChallengeDto("First", "First description", "First code");

        Mockito.when(repository.findById("#FIRST1")).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(ResponseStatusException.class, () -> service.update("#FIRST1", challengeDto));
    }

    @Test
    void testDelete() {
        // Arrange
        ChallengeEntity entity = new ChallengeEntity();
        entity.setId("#FIRST1");
        entity.setName("First");
        entity.setDescription("First description");
        entity.setCode("First code");

        Mockito.when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        // Act
        service.delete("#FIRST1");

        // Assert
        Mockito.verify(repository).delete(entity);
    }

    @Test
    void testDeleteNotFound() {
        // Arrange
        Mockito.when(repository.findById("#FIRST1")).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(ResponseStatusException.class, () -> service.delete("#FIRST1"));
    }
}
