package br.com.eterniaserver.ffut.domain.challenge.services;

import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeAnswerEntity;
import br.com.eterniaserver.ffut.domain.challenge.entities.ProcessRunner;
import br.com.eterniaserver.ffut.domain.challenge.entities.ResultCondenser;

import org.springframework.stereotype.Service;

@Service
public class TestRunnerService {

    public ProcessRunner run(ChallengeAnswerEntity challengeAnswer) {
        return new ProcessRunner(challengeAnswer);
    }

    public ResultCondenser condense(ProcessRunner processRunner) {
        return new ResultCondenser(
                processRunner.getResultOutputPath(),
                processRunner.getJacocoOutputPath(),
                processRunner.getPitestOutputPath()
        );
    }
}
