package org.ahavah.portal.repositories;
import java.time.OffsetDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import org.ahavah.portal.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> getLessonsByDivision(String division);

    List<Lesson> getLessonsByScheduleBeforeAndDivision(OffsetDateTime schedule,String division);


}
