package org.ahavah.portal.repositories;

import org.ahavah.portal.dtos.exercise.UpdateExerciseDto;
import org.ahavah.portal.entities.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

  List<Exercise> getExerciseByScheduleBeforeAndDivision(OffsetDateTime schedule, String division);

  List<Exercise> getExercisesByDivision(String division);


}
