package br.com.eterniaserver.ffut.domain.challenge.testrunner;

import br.com.eterniaserver.ffut.domain.challenge.enums.MutationType;
import br.com.eterniaserver.ffut.domain.challenge.models.MutationResultModel;

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

        MutationResultModel first = resultCondenser.getResultModel().getMutationResults().getFirst();
        MutationResultModel last = resultCondenser.getResultModel().getMutationResults().getLast();

        Assertions.assertEquals(MutationType.MATH, first.getMutationType());
        Assertions.assertEquals("sum", first.getMutationInfo());
        Assertions.assertEquals(3, first.getLine());
        Assertions.assertTrue(first.getIsKilled());

        Assertions.assertEquals(MutationType.PRIMITIVE_RETURNS, last.getMutationType());
        Assertions.assertEquals("sum", last.getMutationInfo());
        Assertions.assertEquals(3, last.getLine());
        Assertions.assertTrue(last.getIsKilled());
    }
}
