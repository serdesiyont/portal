package org.ahavah.portal.mappers;

import org.ahavah.portal.dtos.lesson.CreateLessonRequest;
import org.ahavah.portal.dtos.lesson.LessonDto;
import org.ahavah.portal.entities.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface LessonMapper {


    @Mapping(target = "postedBy", ignore = true)
    Lesson toEntity(CreateLessonRequest createLessonRequest);

    @Mapping(source = "postedBy", target = "user")
    LessonDto toDto(Lesson lesson);

    List<LessonDto> toDtos(List<Lesson> lessons);
}
