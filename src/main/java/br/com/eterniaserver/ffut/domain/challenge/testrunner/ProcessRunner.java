package br.com.eterniaserver.ffut.domain.challenge.testrunner;

import br.com.eterniaserver.ffut.domain.challenge.entities.ChallengeAnswerEntity;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessRunner {

    private static final String POM_FILE = """
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
              <modelVersion>4.0.0</modelVersion>
              <groupId>br.com.eterniaserver</groupId>
              <artifactId>TestRunner</artifactId>
              <version>1.0-SNAPSHOT</version>
              <properties>
                <maven.compiler.source>22</maven.compiler.source>
                <maven.compiler.target>22</maven.compiler.target>
                <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
              </properties>
              <dependencies>
                <dependency>
                  <groupId>org.junit.jupiter</groupId>
                  <artifactId>junit-jupiter-engine</artifactId>
                  <version>5.9.1</version>
                  <scope>test</scope>
                </dependency>
              </dependencies>
              <build>
                <plugins>
                  <plugin>
                    <groupId>org.jacoco</groupId>
                    <artifactId>jacoco-maven-plugin</artifactId>
                    <version>0.8.12</version>
                    <executions>
                      <execution>
                        <goals>
                          <goal>prepare-agent</goal>
                        </goals>
                      </execution>
                      <execution>
                        <id>report-csv</id>
                        <goals>
                          <goal>report</goal>
                        </goals>
                        <configuration>
                          <outputEncoding>UTF-8</outputEncoding>
                          <formats>
                            <format>CSV</format>
                          </formats>
                        </configuration>
                        <phase>prepare-package</phase>
                      </execution>
                    </executions>
                  </plugin>
                  <plugin>
                    <groupId>org.pitest</groupId>
                    <artifactId>pitest-maven</artifactId>
                    <version>1.16.1</version>
                    <configuration>
                      <targetClasses>
                        <param>Main</param>
                      </targetClasses>
                      <targetTests>
                        <param>MainTest</param>
                      </targetTests>
                      <outputFormats>
                        <outputFormat>csv</outputFormat>
                      </outputFormats>
                    </configuration>
                    <dependencies>
                      <dependency>
                      <groupId>org.pitest</groupId>
                      <artifactId>pitest-junit5-plugin</artifactId>
                      <version>1.2.1</version>
                      </dependency>
                    </dependencies>
                  </plugin>
                </plugins>
              </build>
            </project>
    """;

    @Getter
    private String pitestOutputPath;
    @Getter
    private String jacocoOutputPath;
    @Getter
    private String resultOutputPath;

    private final ChallengeAnswerEntity answer;

    public ProcessRunner(ChallengeAnswerEntity answer) {
        this.answer = answer;

        try {
            run();
        } catch (IOException | InterruptedException ignored) { }
    }

    private void run() throws IOException, InterruptedException {
        List<ProcessBuilder> builders = new ArrayList<>();

        String tempDir = System.getProperty("java.io.tmpdir") + FileSystems.getDefault().getSeparator();

        builders.add(new ProcessBuilder().command(
                "bash",
                "-c",
                "mkdir -p " + tempDir + answer.getId() +
                        " && echo '" + POM_FILE + "' > " + tempDir + answer.getId() + "/pom.xml" +
                        " && mkdir -p " + tempDir + answer.getId() + "/src/main/java" +
                        " && mkdir -p " + tempDir + answer.getId() + "/src/test/java" +
                        " && echo '" + answer.getChallengeCode() + "' > " + tempDir + answer.getId() + "/src/main/java/Main.java" +
                        " && echo '" + answer.getUserTestCode() + "' > " + tempDir + answer.getId() + "/src/test/java/MainTest.java"
        ));
        builders.add(new ProcessBuilder().command(
                "bash",
                "-c",
                "cd " + tempDir + answer.getId() + " && mvn clean package && mvn org.pitest:pitest-maven:mutationCoverage"
        ));

        List<Process> processes = ProcessBuilder.startPipeline(builders);

        for (Process process : processes) {
            process.waitFor(60, TimeUnit.SECONDS);
        }

        pitestOutputPath = tempDir + answer.getId() + "/target/pit-reports/mutations.csv";
        jacocoOutputPath = tempDir + answer.getId() + "/target/site/jacoco/jacoco.csv";
        resultOutputPath = tempDir + answer.getId() + "/target/surefire-reports/MainTest.txt";
    }
}
