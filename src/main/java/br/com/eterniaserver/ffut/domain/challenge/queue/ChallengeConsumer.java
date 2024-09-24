package br.com.eterniaserver.ffut.domain.challenge.queue;

import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeAnswerEntity;
import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeEntity;
import br.com.eterniaserver.ffut.domain.challenge.entities.ProcessRunnerEntity;
import br.com.eterniaserver.ffut.domain.challenge.entities.ResultCondenserEntity;
import br.com.eterniaserver.ffut.domain.challenge.repositories.ChallengeAnswerRepository;
import br.com.eterniaserver.ffut.domain.challenge.repositories.ChallengeRepository;
import br.com.eterniaserver.ffut.domain.challenge.services.TestRunnerService;

import lombok.Data;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Data
@Service
public class ChallengeConsumer {

    private static final Logger LOGGER = Logger.getLogger(ChallengeConsumer.class.getName());

    private final ChallengeAnswerRepository challengeAnswerRepository;
    private final ChallengeRepository challengeRepository;
    private final TestRunnerService testRunnerService;

    @RabbitListener(queues = "challenge")
    public void receiveChallenger(String challengeAnswerId) {
        Optional<ChallengeAnswerEntity> challengeAnswerOptional = challengeAnswerRepository.findById(challengeAnswerId);

        if (challengeAnswerOptional.isEmpty()) {
            LOGGER.warning("Challenge answer not found: " + challengeAnswerId);
            return;
        }

        ChallengeAnswerEntity answer = challengeAnswerOptional.get();

        condenseAndProcess(answer);

        Optional<ChallengeEntity> challengeOptional = challengeRepository.findById(answer.getChallengeId());

        if (challengeOptional.isEmpty()) {
            LOGGER.warning("Challenge not found: " + answer.getChallengeId());
            return;
        }

        ChallengeEntity challenge = challengeOptional.get();

        challenge.addToRank(answer);

        challengeRepository.save(challenge);
    }

    private void condenseAndProcess(ChallengeAnswerEntity challengeAnswer) {
        ProcessRunnerEntity processRunner = testRunnerService.run(challengeAnswer);

        ResultCondenserEntity resultCondenser = testRunnerService.condense(processRunner);

        resultCondenser.condenseResults();
        resultCondenser.generateScore();

        challengeAnswer.setChallengeResult(resultCondenser.getResultModel());

        challengeAnswerRepository.save(challengeAnswer);
    }
}
