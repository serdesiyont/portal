package org.ahavah.portal.mappers;

import org.ahavah.portal.dtos.exercise.CreateExerciseRequest;
import org.ahavah.portal.dtos.exercise.ExerciseAllDto;
import org.ahavah.portal.dtos.exercise.ExerciseDto;
import org.ahavah.portal.dtos.exercise.UpdateExerciseDto;
import org.ahavah.portal.entities.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ExerciseMapper {
    @Mapping(target = "postedBy", ignore = true)
    Exercise toEntity(CreateExerciseRequest createExerciseRequest);

    @Mapping(target = "postedBy", ignore = true)
    Exercise toUpdate(UpdateExerciseDto  updateExerciseDto);

    @Mapping(source = "postedBy", target = "user")
    ExerciseDto toDto(Exercise exercise);

    List<ExerciseDto> toDto(List<Exercise> exercises);

    @Mapping(source = "postedBy", target = "user")
    ExerciseAllDto toDtoAll(Exercise exercise);

    List<ExerciseAllDto> toDtoAll(List<Exercise> exercises);
}
