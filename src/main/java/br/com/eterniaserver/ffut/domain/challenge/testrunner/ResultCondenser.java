package br.com.eterniaserver.ffut.domain.challenge.testrunner;

import br.com.eterniaserver.ffut.domain.challenge.enums.MutationType;
import br.com.eterniaserver.ffut.domain.challenge.models.ChallengeResultModel;
import br.com.eterniaserver.ffut.domain.challenge.models.MutationResultModel;

import lombok.Getter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultCondenser {

    private static final Logger LOGGER = Logger.getLogger(ResultCondenser.class.getName());

    private static final int TESTS_RUNS = 0;
    private static final int TESTS_FAILED = 1;
    private static final int TESTS_ERROR = 2;

    private static final int INSTRUCTION_MISSED = 3;
    private static final int INSTRUCTION_COVERAGE = 4;
    private static final int BRANCH_MISSED = 5;
    private static final int BRANCH_COVERAGE = 6;
    private static final int LINE_MISSED = 7;
    private static final int LINE_COVERAGE = 8;
    private static final int COMPLEXITY_MISSED = 9;
    private static final int COMPLEXITY_COVERAGE = 10;
    private static final int METHOD_MISSED = 11;
    private static final int METHOD_COVERAGE = 12;

    private static final int MUTATION_TYPE = 2;
    private static final int MUTATION_INFO = 3;
    private static final int MUTATION_LINE = 4;
    private static final int MUTATION_STATUS = 5;

    @Getter
    private final ChallengeResultModel resultModel = new ChallengeResultModel();

    private final String resultOutputPath;
    private final String jacocoOutputPath;
    private final String pitestOutputPath;

    public ResultCondenser(String resultOutputPath,
                           String jacocoOutputPath,
                           String pitestOutputPath) {
        this.resultOutputPath = resultOutputPath;
        this.jacocoOutputPath = jacocoOutputPath;
        this.pitestOutputPath = pitestOutputPath;
    }

    public void condenseResults() {
        this.readDefaultTest();
        this.readJacocoCoverage();
        this.readPitestMutationData();
    }

    public void generateScore() {

    }

    private void readPitestMutationData() {
        File file = new File(pitestOutputPath);

        List<MutationResultModel> mutationResults = new ArrayList<>();

        try {
            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

            for (String line : lines) {
                if (line.isEmpty()) {
                    break;
                }

                String[] data = line.split(",");

                MutationResultModel mutationResult = new MutationResultModel();

                mutationResult.setMutationType(MutationType.getEnum(data[MUTATION_TYPE]));
                mutationResult.setMutationInfo(data[MUTATION_INFO]);
                mutationResult.setIsKilled(data[MUTATION_STATUS].equals("KILLED"));
                mutationResult.setLine(parseInt(data[MUTATION_LINE]));

                mutationResults.add(mutationResult);
            }
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage());
        }

        resultModel.setMutationResults(mutationResults);
    }

    private void readJacocoCoverage() {
        File file = new File(jacocoOutputPath);

        try {
            String[] data = FileUtils
                    .readLines(file, StandardCharsets.UTF_8)
                    .get(1)
                    .split(",");

            resultModel.setInstructionCoverage(parseInt(data[INSTRUCTION_COVERAGE]));
            resultModel.setInstructionMissed(parseInt(data[INSTRUCTION_MISSED]));
            resultModel.setBranchCoverage(parseInt(data[BRANCH_COVERAGE]));
            resultModel.setBranchMissed(parseInt(data[BRANCH_MISSED]));
            resultModel.setLineCoverage(parseInt(data[LINE_COVERAGE]));
            resultModel.setLineMissed(parseInt(data[LINE_MISSED]));
            resultModel.setComplexityCoverage(parseInt(data[COMPLEXITY_COVERAGE]));
            resultModel.setComplexityMissed(parseInt(data[COMPLEXITY_MISSED]));
            resultModel.setMethodCoverage(parseInt(data[METHOD_COVERAGE]));
            resultModel.setMethodMissed(parseInt(data[METHOD_MISSED]));
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage());

            resultModel.setInstructionCoverage(0);
            resultModel.setInstructionMissed(0);
            resultModel.setBranchCoverage(0);
            resultModel.setBranchMissed(0);
            resultModel.setLineCoverage(0);
            resultModel.setLineMissed(0);
            resultModel.setComplexityCoverage(0);
            resultModel.setComplexityMissed(0);
            resultModel.setMethodCoverage(0);
            resultModel.setMethodMissed(0);
        }
    }

    private void readDefaultTest() {
        File file = new File(resultOutputPath);

        try {
            String[] data = FileUtils
                    .readLines(file, StandardCharsets.UTF_8)
                    .get(3)
                    .split(", ");

            resultModel.setTestsSuccess(getTestValue(data, TESTS_RUNS));
            resultModel.setTestsFailed(getTestValue(data, TESTS_FAILED));
            resultModel.setTestsError(getTestValue(data, TESTS_ERROR));
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage());

            resultModel.setTestsSuccess(0);
            resultModel.setTestsFailed(0);
            resultModel.setTestsError(0);
        }
    }

    private int getTestValue(String[] data, int index) {
        return parseInt(data[index].split(": ")[1].trim());
    }

    private int parseInt(String value) {
        return Integer.parseInt(value);
    }
}