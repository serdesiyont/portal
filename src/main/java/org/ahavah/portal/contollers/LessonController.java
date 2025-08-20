package org.ahavah.portal.contollers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.ahavah.portal.dtos.lesson.CreateLessonRequest;
import org.ahavah.portal.dtos.lesson.LessonDto;
import org.ahavah.portal.services.LessonService;
import org.ahavah.portal.services.R2Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;



    @PreAuthorize("hasRole('MENTOR')")
    @PostMapping( consumes =  {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> handleFileUpload(
            @Valid @ModelAttribute CreateLessonRequest createLessonRequest,
            @RequestParam("file") MultipartFile file
            ) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        if(!file.getContentType().equals("text/markdown")){
            return ResponseEntity.badRequest().body("File must be a markdown file");
        }

        var upload = this.lessonService.uploadLesson(file, createLessonRequest);
        return ResponseEntity.ok(upload);
        }


    @PreAuthorize("hasRole('MENTOR') or hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<LessonDto>> getLessons(
            @RequestParam(value = "division") String division
    ) {
        if(division == null || division.isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }

        var lessons = this.lessonService.getLessons(division);

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

}
