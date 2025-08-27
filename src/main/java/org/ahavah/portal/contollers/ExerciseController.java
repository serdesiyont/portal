package org.ahavah.portal.contollers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.exercise.CreateExerciseRequest;
import org.ahavah.portal.dtos.exercise.ExerciseDto;
import org.ahavah.portal.services.ExerciseServices;
import org.ahavah.portal.services.PistonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercises")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseServices exerciseServices;
    private final PistonService pistonService;

    @PostMapping
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ExerciseDto> createExercise(
            @Valid  @RequestBody CreateExerciseRequest createExerciseRequest) {

        var exercise = exerciseServices.createExercise(createExerciseRequest);
        return ResponseEntity.ok().body(exercise);

    }

    @GetMapping
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<List<ExerciseDto>> getExercises(){
        return ResponseEntity.ok().body(exerciseServices.getExercises());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<String> deleteExercise(@PathVariable Long id){
        return ResponseEntity.ok().body(exerciseServices.deleteExercise(id));

    }

    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    public ResponseEntity<String> submitExercise(
            @RequestBody  ExerciseDto exerciseDto
    ){
        System.out.println(exerciseDto.getBoilerplate());
        try {
            var res = this.pistonService.executeCode(exerciseDto);
            return ResponseEntity.ok().body(res);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
