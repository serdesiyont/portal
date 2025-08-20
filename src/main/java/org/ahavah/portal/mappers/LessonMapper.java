package org.ahavah.portal.mappers;

import org.ahavah.portal.dtos.lesson.CreateLessonRequest;
import org.ahavah.portal.dtos.lesson.LessonDto;
import org.ahavah.portal.entities.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {



    @Mapping(target = "postedBy", ignore = true)
    Lesson toEntity(CreateLessonRequest createLessonRequest);
    LessonDto toDto(Lesson lesson);
}
