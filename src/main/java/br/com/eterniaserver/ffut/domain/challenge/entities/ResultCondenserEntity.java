package br.com.eterniaserver.ffut.domain.challenge.entities;

import br.com.eterniaserver.ffut.domain.challenge.enums.MutationType;
import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeAnswerEntity.ChallengeResultEntity;
import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeAnswerEntity.MutationResultEntity;

import lombok.Getter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultCondenserEntity {

    private static final Logger LOGGER = Logger.getLogger(ResultCondenserEntity.class.getName());

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
    private final ChallengeResultEntity resultModel = new ChallengeResultEntity();

    private final String resultOutputPath;
    private final String jacocoOutputPath;
    private final String pitestOutputPath;

    public ResultCondenserEntity(String resultOutputPath,
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
        int totalTests = resultModel.getTestsSuccess() + resultModel.getTestsFailed() + resultModel.getTestsError();

        double testScore = totalTests > 0 ? (double) resultModel.getTestsSuccess() / totalTests : 0.0;

        if (totalTests != resultModel.getTestsSuccess()) {
            resultModel.setScore(0.2 * testScore * 100);
            return;
        }

        double instructionScore = calculateCoverageScore(
                resultModel.getInstructionCoverage(),
                resultModel.getInstructionMissed()
        );

        double branchScore = calculateCoverageScore(resultModel.getBranchCoverage(), resultModel.getBranchMissed());

        double lineScore = calculateCoverageScore(resultModel.getLineCoverage(), resultModel.getLineMissed());

        double complexityScore = calculateCoverageScore(
                resultModel.getComplexityCoverage(),
                resultModel.getComplexityMissed()
        );

        double methodScore = calculateCoverageScore(resultModel.getMethodCoverage(), resultModel.getMethodMissed());

        double mutationScore = calculateMutationScore(resultModel.getMutationResults());

        double finalScore = (0.2 * testScore) +
                (0.1 * instructionScore) +
                (0.1 * branchScore) +
                (0.1 * lineScore) +
                (0.2 * complexityScore) +
                (0.1 * methodScore) +
                (0.2 * mutationScore);

        resultModel.setScore(100 * finalScore);
    }

    private double calculateCoverageScore(int covered, int missed) {
        int total = covered + missed;
        return total > 0 ? (double) covered / total : 1.0;
    }

    private double calculateMutationScore(List<MutationResultEntity> mutationResults) {
        if (mutationResults.isEmpty()) {
            return 0.0;
        }

        double totalWeight = 0.0;
        double weightedKilledCount = 0.0;

        for (MutationResultEntity mutation : mutationResults) {
            double weight = getMutationWeight(mutation.getMutationType());
            totalWeight += weight;
            if (mutation.getIsKilled()) {
                weightedKilledCount += weight;
            }
        }

        return totalWeight > 0 ? weightedKilledCount / totalWeight : 0.0;
    }

    private double getMutationWeight(MutationType mutationType) {
        return switch (mutationType) {
            case CONDITIONAL_BOUNDARY -> 1.0;
            case NEGATE_CONDITIONALS -> 0.9;
            case MATH -> 0.8;
            case INCREMENTS, INVERT_NEGATIVES -> 0.7;
            case VOID_METHOD_CALLS -> 0.6;
            case EMPTY_RETURNS, NULL_RETURNS -> 0.5;
            case PRIMITIVE_RETURNS -> 0.4;
            case TRUE_RETURNS, FALSE_RETURNS -> 0.3;
        };
    }

    private void readPitestMutationData() {
        File file = new File(pitestOutputPath);

        List<MutationResultEntity> mutationResults = new ArrayList<>();

        try {
            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

            for (String line : lines) {
                if (line.isEmpty()) {
                    break;
                }

                String[] data = line.split(",");

                MutationResultEntity mutationResult = new MutationResultEntity();

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
