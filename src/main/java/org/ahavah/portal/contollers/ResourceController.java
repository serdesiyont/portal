package org.ahavah.portal.contollers;


import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.resource.ResourceDto;
import org.ahavah.portal.services.ResourceServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceServices resourceServices;


    @PreAuthorize("hasRole('MENTOR')")
    @PostMapping( consumes =  {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> handleFileUpload(
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file
    ) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        if(!file.getContentType().equals("application/pdf")){
            return ResponseEntity.badRequest().body("File must be a application/pdf file, you provided a " + file.getContentType());
        }

        if(file.getSize() > 5000000){
            return ResponseEntity.badRequest().body("File limit of 5MB exceeded");
        }

        var upload = this.resourceServices.uploadLesson(file,title);
        return ResponseEntity.ok(upload);
    }


    @PreAuthorize("hasRole('MENTOR') or hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<ResourceDto>> getLessons(

    ) {


        var resources = this.resourceServices.getResources();

        if (resources.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("hasRole('MENTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable Long id) {
        var deleted = this.resourceServices.deleteLesson(id);
        return ResponseEntity.ok(deleted);
    }

}
