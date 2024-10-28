package br.com.eterniaserver.ffut.domain.challenge.entities;

import br.com.eterniaserver.ffut.domain.challenge.enums.MutationType;
import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeAnswerEntity.ChallengeResultEntity;
import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeAnswerEntity.MutationResultEntity;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResultCondenserEntityTest {

    private static final String RESULT_OUTPUT_PATH = "src/test/resources/MainTest.txt";
    private static final String JACOCO_OUTPUT_PATH = "src/test/resources/jacoco.csv";
    private static final String PITEST_OUTPUT_PATH = "src/test/resources/mutations.csv";

    @Test
    void validateDefaultResultsCondenser() {
        ResultCondenserEntity resultCondenser = new ResultCondenserEntity(
                RESULT_OUTPUT_PATH,
                JACOCO_OUTPUT_PATH,
                PITEST_OUTPUT_PATH
        );

        resultCondenser.condenseResults();

        Assertions.assertNotNull(resultCondenser.getResultModel());
        Assertions.assertEquals(3, resultCondenser.getResultModel().getTestsSuccess());
        Assertions.assertEquals(0, resultCondenser.getResultModel().getTestsFailed());
        Assertions.assertEquals(0, resultCondenser.getResultModel().getTestsError());
    }

    @Test
    void validateJacocoResultsCondenser() {
        ResultCondenserEntity resultCondenser = new ResultCondenserEntity(
                RESULT_OUTPUT_PATH,
                JACOCO_OUTPUT_PATH,
                PITEST_OUTPUT_PATH
        );

        resultCondenser.condenseResults();

        Assertions.assertNotNull(resultCondenser.getResultModel());
        Assertions.assertEquals(0, resultCondenser.getResultModel().getInstructionMissed());
        Assertions.assertEquals(7, resultCondenser.getResultModel().getInstructionCoverage());
        Assertions.assertEquals(0, resultCondenser.getResultModel().getBranchMissed());
        Assertions.assertEquals(0, resultCondenser.getResultModel().getBranchCoverage());
        Assertions.assertEquals(0, resultCondenser.getResultModel().getLineMissed());
        Assertions.assertEquals(2, resultCondenser.getResultModel().getLineCoverage());
        Assertions.assertEquals(0, resultCondenser.getResultModel().getComplexityMissed());
        Assertions.assertEquals(2, resultCondenser.getResultModel().getComplexityCoverage());
        Assertions.assertEquals(0, resultCondenser.getResultModel().getMethodMissed());
        Assertions.assertEquals(2, resultCondenser.getResultModel().getMethodCoverage());
    }

    @Test
    void validatePitestMutationsResultsCondenser() {
        ResultCondenserEntity resultCondenser = new ResultCondenserEntity(
                RESULT_OUTPUT_PATH,
                JACOCO_OUTPUT_PATH,
                PITEST_OUTPUT_PATH
        );

        resultCondenser.condenseResults();

        Assertions.assertNotNull(resultCondenser.getResultModel());
        Assertions.assertEquals(2, resultCondenser.getResultModel().getMutationResults().size());

        MutationResultEntity first = resultCondenser.getResultModel().getMutationResults().get(0);
        MutationResultEntity last = resultCondenser.getResultModel().getMutationResults().get(1);

        Assertions.assertEquals(MutationType.MATH, first.getMutationType());
        Assertions.assertEquals("sum", first.getMutationInfo());
        Assertions.assertEquals(3, first.getLine());
        Assertions.assertTrue(first.getIsKilled());

        Assertions.assertEquals(MutationType.PRIMITIVE_RETURNS, last.getMutationType());
        Assertions.assertEquals("sum", last.getMutationInfo());
        Assertions.assertEquals(3, last.getLine());
        Assertions.assertTrue(last.getIsKilled());
    }

     @Test
    void validateGenerateScore() {
        ResultCondenserEntity resultCondenser = new ResultCondenserEntity(
                RESULT_OUTPUT_PATH,
                JACOCO_OUTPUT_PATH,
                PITEST_OUTPUT_PATH
        );

        ChallengeResultEntity resultModel = resultCondenser.getResultModel();
        resultModel.setTestsSuccess(4);
        resultModel.setTestsFailed(1);
        resultModel.setTestsError(1);

        resultModel.setInstructionCoverage(80);
        resultModel.setInstructionMissed(20);
        resultModel.setBranchCoverage(70);
        resultModel.setBranchMissed(30);
        resultModel.setLineCoverage(90);
        resultModel.setLineMissed(10);
        resultModel.setComplexityCoverage(85);
        resultModel.setComplexityMissed(15);
        resultModel.setMethodCoverage(75);
        resultModel.setMethodMissed(25);

        List<MutationResultEntity> mutationResults = new ArrayList<>();
        MutationResultEntity mutation1 = new MutationResultEntity();
        mutation1.setMutationType(MutationType.MATH);
        mutation1.setMutationInfo("addition");
        mutation1.setIsKilled(true);
        mutation1.setLine(5);

        MutationResultEntity mutation2 = new MutationResultEntity();
        mutation2.setMutationType(MutationType.PRIMITIVE_RETURNS);
        mutation2.setMutationInfo("return");
        mutation2.setIsKilled(false);
        mutation2.setLine(10);

        mutationResults.add(mutation1);
        mutationResults.add(mutation2);

        resultModel.setMutationResults(mutationResults);

        resultCondenser.generateScore();

        double expectedScore = (0.2 * (4.0 / 6)) +  
                                (0.1 * (80.0 / 100)) +
                                (0.1 * (70.0 / 100)) +
                                (0.1 * (90.0 / 100)) +
                                (0.2 * (85.0 / 100)) +
                                (0.1 * (75.0 / 100)) +
                                (0.2 * (0.8 / 1.2));

        expectedScore *= 100;

        Assertions.assertEquals(expectedScore, resultModel.getScore(), 0.01);
    }

    @Test
    void testingWhy90Percent() {
        ResultCondenserEntity resultCondenser = new ResultCondenserEntity(
                RESULT_OUTPUT_PATH,
                JACOCO_OUTPUT_PATH,
                PITEST_OUTPUT_PATH
        );

        ChallengeResultEntity resultModel = resultCondenser.getResultModel();
        resultModel.setTestsSuccess(1);
        resultModel.setTestsFailed(0);
        resultModel.setTestsError(0);

        resultModel.setInstructionCoverage(7);
        resultModel.setInstructionMissed(0);
        resultModel.setBranchCoverage(0);
        resultModel.setBranchMissed(0);
        resultModel.setLineCoverage(2);
        resultModel.setLineMissed(0);
        resultModel.setComplexityCoverage(2);
        resultModel.setComplexityMissed(0);
        resultModel.setMethodCoverage(2);
        resultModel.setMethodMissed(0);

        List<MutationResultEntity> mutationResults = new ArrayList<>();
        MutationResultEntity mutation1 = new MutationResultEntity();
        mutation1.setMutationType(MutationType.MATH);
        mutation1.setMutationInfo("sum");
        mutation1.setIsKilled(true);
        mutation1.setLine(3);

        MutationResultEntity mutation2 = new MutationResultEntity();
        mutation2.setMutationType(MutationType.PRIMITIVE_RETURNS);
        mutation2.setMutationInfo("sum");
        mutation2.setIsKilled(true);
        mutation2.setLine(3);

        mutationResults.add(mutation1);
        mutationResults.add(mutation2);

        resultModel.setMutationResults(mutationResults);

        resultCondenser.generateScore();

        double expectedScore = (0.2 * (1/1)) +  
                                (0.1 * (7/7)) +
                                (0.1 * (1/1)) +
                                (0.1 * (2/2)) +
                                (0.2 * (2/2)) +
                                (0.1 * (2/2)) +
                                (0.2 * (2/2));

        expectedScore *= 100;

        Assertions.assertEquals(expectedScore, resultModel.getScore(), 0.01);
    }
}
