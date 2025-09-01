package org.ahavah.portal.services;

import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.exercise.ExerciseDto;
import org.ahavah.portal.entities.Submission;
import org.ahavah.portal.repositories.ExerciseRepository;
import org.ahavah.portal.repositories.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SubmissionServices {
    private final ExerciseRepository exerciseRepository;
    private final PistonService pistonService;
    private final UserServices userServices;
    private final SubmissionRepository submissionRepository;



    private Map<String, Object> checkCodeStatus(ExerciseDto exerciseDto, String testCase, String output) {
        Map<String, Object> result = new HashMap<>();
        try {
            var submittedCode = exerciseDto.getBoilerplate().get("code").toString();
            var added = submittedCode + "\n" + testCase;
            exerciseDto.getBoilerplate().put("code", added);
            var executionOutput = this.pistonService.executeCode(exerciseDto);
            if (executionOutput.equals(output + "\n")) {
                result.put("status", "PASSED");
                return result;
            }
            result.put("status", "FAILED");
            result.put("expected", output);
            result.put("code", executionOutput);
            return result;

        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("code", e.getMessage());
            return result;
        }


    }

    public Map<String, Object> executor(ExerciseDto exerciseDto) {
        var exercise = this.exerciseRepository.findById(exerciseDto.getId())
                .orElseThrow(() -> new RuntimeException("exercise not found"));
        Map<String, Object> testCases = exercise.getTestCases();
        Map<String, Object> results = new HashMap<>();
        String overallStatus = "PASSED";
        for (Map.Entry<String, Object> entry : testCases.entrySet()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> testCaseDetails = (Map<String, Object>) entry.getValue();
            String testCode = (String) testCaseDetails.get("test");

            String outputList = (String) testCaseDetails.get("output");
            String expectedOutput = String.join("\n", outputList);
            var newExerciseDto = new ExerciseDto();
            newExerciseDto.setBoilerplate(new HashMap<>(exerciseDto.getBoilerplate()));
            newExerciseDto.setLanguage(exerciseDto.getLanguage());
            Map<String, Object> executionResult = checkCodeStatus(newExerciseDto, testCode, expectedOutput);
            results.put(entry.getKey(), executionResult);
            if ("FAILED".equals(executionResult.get("status"))) {
                overallStatus = "FAILED"; // Mark overall status as FAILED
            }
        }
        var submission = new Submission();
        submission.setExercise(exercise);
        submission.setStudent(userServices.currentUser());
        submission.setSubmittedCode(exerciseDto.getBoilerplate());
        submission.setSubmittedAt(OffsetDateTime.now());
        submission.setStatus(overallStatus);

        this.submissionRepository.save(submission);
        return results;
    }




}
