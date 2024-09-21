package br.com.eterniaserver.ffut.domain.challenge.entities;

import br.com.eterniaserver.ffut.domain.challenge.models.ChallengeRank;
import br.com.eterniaserver.ffut.domain.challenge.models.ChallengeResultModel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ChallengeEntityTest {

    private ChallengeEntity challengeEntity;

    @BeforeEach
    void setUp() {
        challengeEntity = new ChallengeEntity();
    }

    @Test
    void shouldSortWhenAddToRanking() {
        // Arrange

        setChallengeRanks();

        ChallengeAnswerEntity newAnswer = new ChallengeAnswerEntity();
        ChallengeResultModel newResult = new ChallengeResultModel();
        
        newResult.setScore(100.0D);
        
        newAnswer.setChallengeResult(newResult);
        newAnswer.setUserId("123");
        newAnswer.setUsername("test");

        // Act

        challengeEntity.addToRank(newAnswer);

        // Assert

        List<ChallengeRank> rank = challengeEntity.getRank();

        Assertions.assertEquals(3, rank.size());

        Assertions.assertEquals(100.0D, rank.getFirst().getChallengeResultModel().getScore());
        Assertions.assertEquals(75.0D, rank.get(1).getChallengeResultModel().getScore());
        Assertions.assertEquals(50.0D, rank.getLast().getChallengeResultModel().getScore());
    }

    private void setChallengeRanks() {
        ChallengeRank oldFirst = new ChallengeRank();
        ChallengeResultModel oldFirstResult = new ChallengeResultModel();

        oldFirstResult.setScore(75.0D);

        oldFirst.setChallengeResultModel(oldFirstResult);

        ChallengeRank oldSecond = new ChallengeRank();
        ChallengeResultModel oldSecondResult = new ChallengeResultModel();

        oldSecondResult.setScore(50.0D);

        oldSecond.setChallengeResultModel(oldSecondResult);

        challengeEntity.setRank(new ArrayList<>(List.of(oldFirst, oldSecond)));
    }

}
