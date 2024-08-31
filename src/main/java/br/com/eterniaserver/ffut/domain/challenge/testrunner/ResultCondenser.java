package br.com.eterniaserver.ffut.domain.challenge.testrunner;

import java.util.List;

public class ResultCondenser {

    private static final String MUTATED_COVERAGE = ">> Line Coverage (for mutated classes only): ";

    private final List<String> pitestReport;

    public ResultCondenser(List<String> pitestReport) {
        this.pitestReport = pitestReport;
    }

    private void condense() {
        for (String line : pitestReport) {
            if (line.startsWith(MUTATED_COVERAGE)) {

                System.out.println(line);
            } else if (line.startsWith(">> Generated ")){
                System.out.println(line);
            }
        }
    }

    private void readMutatedCoverage(String line) {
        line = line.replace(MUTATED_COVERAGE, "");

        String[] coverages = line.split(" ")[0].split("/");

        int covered = Integer.parseInt(coverages[0]);
        int total = Integer.parseInt(coverages[1]);
    }
}
