package org.ahavah.portal.services;

import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.lesson.CreateLessonRequest;
import org.ahavah.portal.dtos.lesson.LessonDto;
import org.ahavah.portal.mappers.LessonMapper;
import org.ahavah.portal.mappers.UserMapper;
import org.ahavah.portal.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServices {

    private final R2Service r2Service;
    private final LessonMapper lessonMapper;
    private final LessonRepository lessonRepository;
    private final UserServices userServices;
    private final UserMapper userMapper;


    @Value("${cloudflare.r2.bucket-lessons}")
    String bucketName;

    @Value("${cloudflare.r2.bucket-lessons-url}")
    String bucketUrl;

    public LessonDto uploadLesson(MultipartFile file, CreateLessonRequest createLessonRequest) {

        try {


            var address = this.r2Service.uploadFile(bucketName, bucketUrl, file);
            var curUser = this.userServices.currentUser();
            var lesson = lessonMapper.toEntity(createLessonRequest);
            lesson.setAddress(address);
            lesson.setPostedBy(curUser);
            lesson.setDivision((curUser.getDivision()).toLowerCase());
            lessonRepository.save(lesson);
            var lessonDto = lessonMapper.toDto(lesson);
            lessonDto.setUser(this.userMapper.userDto(curUser));
            return lessonDto;

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public List<LessonDto> getLessons(String division) {
        List<LessonDto> lessonDtos = new ArrayList<>();

        var lessons = lessonRepository.getLessonsByScheduleBeforeAndDivision(OffsetDateTime.now(),division.toLowerCase());

        lessons.forEach(lesson -> {
            var l = lessonMapper.toDto(lesson);
            l.setUser(userMapper.userDto(lesson.getPostedBy()));
            lessonDtos.add(l);
        });

        return lessonDtos;
    }

    public String deleteLesson(Long id) {
        var curUser = userServices.currentUser();
        var lesson = lessonRepository.findById(id).orElse(null);
        if (lesson == null) {
            return "Lesson not found";
        }
        if(!(curUser.getDivision().toLowerCase()).equals(lesson.getDivision())) {
            return "You are not authorized to delete this lesson";
        }
        lessonRepository.delete(lesson);
        return "Lesson deleted successfully";
    }

}
