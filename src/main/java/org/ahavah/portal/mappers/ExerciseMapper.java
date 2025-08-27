package org.ahavah.portal.mappers;

import org.ahavah.portal.dtos.exercise.CreateExerciseRequest;
import org.ahavah.portal.dtos.exercise.ExerciseDto;
import org.ahavah.portal.entities.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ExerciseMapper {
    @Mapping(target = "postedBy", ignore = true)


    Exercise toEntity(CreateExerciseRequest createExerciseRequest);

    @Mapping(source = "postedBy", target = "user")
    ExerciseDto toDto(Exercise exercise);

    List<ExerciseDto> toDto(List<Exercise> exercises);
}
