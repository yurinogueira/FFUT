package br.com.eterniaserver.ffut.domain.challenge.entities;

import br.com.eterniaserver.ffut.domain.challenge.enums.MutationType;
import br.com.eterniaserver.ffut.domain.challenge.models.ChallengeResultModel;
import br.com.eterniaserver.ffut.domain.challenge.models.MutationResultModel;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResultCondenserTest {

    private static final String RESULT_OUTPUT_PATH = "src/test/resources/MainTest.txt";
    private static final String JACOCO_OUTPUT_PATH = "src/test/resources/jacoco.csv";
    private static final String PITEST_OUTPUT_PATH = "src/test/resources/mutations.csv";

    @Test
    void validateDefaultResultsCondenser() {
        ResultCondenser resultCondenser = new ResultCondenser(
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
        ResultCondenser resultCondenser = new ResultCondenser(
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
        ResultCondenser resultCondenser = new ResultCondenser(
                RESULT_OUTPUT_PATH,
                JACOCO_OUTPUT_PATH,
                PITEST_OUTPUT_PATH
        );

        resultCondenser.condenseResults();

        Assertions.assertNotNull(resultCondenser.getResultModel());
        Assertions.assertEquals(2, resultCondenser.getResultModel().getMutationResults().size());

        MutationResultModel first = resultCondenser.getResultModel().getMutationResults().get(0);
        MutationResultModel last = resultCondenser.getResultModel().getMutationResults().get(1);

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
        ResultCondenser resultCondenser = new ResultCondenser(
                RESULT_OUTPUT_PATH,
                JACOCO_OUTPUT_PATH,
                PITEST_OUTPUT_PATH
        );

        ChallengeResultModel resultModel = resultCondenser.getResultModel();
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

        List<MutationResultModel> mutationResults = new ArrayList<>();
        MutationResultModel mutation1 = new MutationResultModel();
        mutation1.setMutationType(MutationType.MATH);
        mutation1.setMutationInfo("addition");
        mutation1.setIsKilled(true);
        mutation1.setLine(5);

        MutationResultModel mutation2 = new MutationResultModel();
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
}
