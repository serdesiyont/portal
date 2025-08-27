package org.ahavah.portal.services;

import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.exercise.CreateExerciseRequest;
import org.ahavah.portal.dtos.exercise.ExerciseDto;
import org.ahavah.portal.mappers.ExerciseMapper;
import org.ahavah.portal.repositories.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseServices {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final UserServices userServices;


    public List<ExerciseDto> getExercises(){
        var user = this.userServices.currentUser();
        var exercises = this.exerciseRepository.getExerciseByScheduleBeforeAndDivision(OffsetDateTime.now(), (user.getDivision()).toLowerCase());
        return exerciseMapper.toDto(exercises);
    }


    public ExerciseDto createExercise(CreateExerciseRequest createExerciseRequest) {
        var user =  this.userServices.currentUser();
        var exercise = exerciseMapper.toEntity(createExerciseRequest);
        exercise.setPostedBy(user);
        exercise.setDivision((user.getDivision()).toLowerCase());
        exerciseRepository.save(exercise);
        return exerciseMapper.toDto(exercise);
    }

    public String deleteExercise(Long id){
        var user = this.userServices.currentUser();
        var exercise = this.exerciseRepository.findById(id).orElse(null);
        if (exercise == null) {
            return "Exercise not found";
        }
        if (!(user.getDivision()).equalsIgnoreCase(exercise.getDivision())) {
            return "You are not authorized to delete this exercise";
        }
        exerciseRepository.delete(exercise);
        return "Exercise deleted";
    }



}
