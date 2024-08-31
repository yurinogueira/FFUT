package br.com.eterniaserver.ffut.domain.challenge.entities;

import br.com.eterniaserver.ffut.domain.challenge.models.ChallengeRank;
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

    private List<ChallengeRank> rank = new ArrayList<>();

    private void AddToRank(ChallengeAnswerEntity answer) {
        ChallengeRank challengeRank = new ChallengeRank();

        challengeRank.setUserId(answer.getUserId());
        challengeRank.setUsername(answer.getUsername());
        challengeRank.setChallengeResultModel(answer.getChallengeResult());

        rank.add(challengeRank);
        rank.sort(Comparator.comparingDouble(r -> r.getChallengeResultModel().getScore()));
    }
}
