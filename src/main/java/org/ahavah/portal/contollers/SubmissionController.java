package org.ahavah.portal.contollers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.exercise.ExerciseDto;
import org.ahavah.portal.services.SubmissionServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/submit")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionServices submissionServices;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    public ResponseEntity<?> submitExercise(
            @Valid @RequestBody ExerciseDto exerciseDto
    ){

         return ResponseEntity.ok(this.submissionServices.executor(exerciseDto));
    }
}
