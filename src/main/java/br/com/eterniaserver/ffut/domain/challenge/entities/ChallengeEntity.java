package br.com.eterniaserver.ffut.domain.challenge.entities;

import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeAnswerEntity.ChallengeResultEntity;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@Document(collection = "challenge")
public class ChallengeEntity {

    @Id
    private String id;

    private String name;

    private String description;

    private Integer challengeVersion;

    private String code;

    private List<ChallengeRankEntity> rank = new ArrayList<>();

    public void incrementChallengeVersion() {
        challengeVersion++;
    }

    public void addToRank(ChallengeAnswerEntity answer) {
        ChallengeRankEntity challengeRankEntity = new ChallengeRankEntity();

        challengeRankEntity.setUserId(answer.getUserId());
        challengeRankEntity.setUsername(answer.getUsername());
        challengeRankEntity.setChallengeResultEntity(answer.getChallengeResult());

        rank.add(challengeRankEntity);

        Comparator<ChallengeRankEntity> comparator = Comparator.comparingDouble(o -> o.getChallengeResultEntity().getScore());

        rank.sort(comparator.reversed());
    }

    @Data
    public static class ChallengeRankEntity {

        private String userId;

        private String username;

        private ChallengeResultEntity challengeResultEntity;
    }

}
