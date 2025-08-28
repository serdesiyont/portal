package org.ahavah.portal.contollers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.ahavah.portal.dtos.lesson.CreateLessonRequest;
import org.ahavah.portal.dtos.lesson.LessonDto;
import org.ahavah.portal.services.LessonServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonServices lessonService;




    @PreAuthorize("hasRole('MENTOR')")
    @PostMapping( consumes =  {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> handleFileUpload(
            @Valid @ModelAttribute CreateLessonRequest createLessonRequest,
            @RequestParam("file") MultipartFile file
            ) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        if(!(Objects.equals(file.getContentType(), "text/markdown")) || file.getSize() > 1000000){
            return ResponseEntity.badRequest().body("File must be a text/markdown file, you provided: " + file.getContentType() + "Size exceeds 1MB");
        }


        var upload = this.lessonService.uploadLesson(file, createLessonRequest);
        return ResponseEntity.ok(upload);
        }


    @PreAuthorize("hasRole('MENTOR') or hasRole('STUDENT')")
    @GetMapping()
    public ResponseEntity<List<LessonDto>> getLessons(
    ) {

        var lessons = this.lessonService.getLessons();

        if (lessons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(lessons);
    }

    @PreAuthorize("hasRole('MENTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable Long id) {
        var deleted = this.lessonService.deleteLesson(id);
        return ResponseEntity.ok(deleted);
    }

    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/mentors")
    public ResponseEntity<List<LessonDto>> getLessonsByDivision(
    ) {

        var lessons = this.lessonService.getAllLessonsByDivision();

        if (lessons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }return ResponseEntity.ok(lessons);
    }

}
