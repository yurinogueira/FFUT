package br.com.eterniaserver.ffut.domain.challenge.queue;

import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeAnswerEntity;
import br.com.eterniaserver.ffut.domain.challenge.repositories.ChallengeAnswerRepository;
import br.com.eterniaserver.ffut.domain.challenge.testrunner.ProcessRunner;

import br.com.eterniaserver.ffut.domain.challenge.testrunner.ResultCondenser;
import lombok.Data;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
public class ChallengeConsumer {

    private final ChallengeAnswerRepository challengeAnswerRepository;

    @RabbitListener(queues = "challenge")
    public void receiveChallenger(String challengeAnswerId) {
        Optional<ChallengeAnswerEntity> challengeAnswerOptional = challengeAnswerRepository.findById(challengeAnswerId);

        if (challengeAnswerOptional.isPresent()) {
            ChallengeAnswerEntity challengeAnswer = challengeAnswerOptional.get();

            ProcessRunner processRunner = new ProcessRunner(challengeAnswer);

            ResultCondenser resultCondenser = new ResultCondenser(
                    processRunner.getResultOutputPath(),
                    processRunner.getJacocoOutputPath(),
                    processRunner.getPitestOutputPath()
            );

            resultCondenser.condenseResults();
            resultCondenser.generateScore();

            challengeAnswer.setChallengeResult(resultCondenser.getResultModel());

            challengeAnswerRepository.save(challengeAnswer);
        }
    }
}
