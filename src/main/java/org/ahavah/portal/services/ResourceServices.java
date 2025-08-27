package org.ahavah.portal.services;

import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.resource.ResourceDto;
import org.ahavah.portal.mappers.ResourceMapper;
import org.ahavah.portal.mappers.UserMapper;
import org.ahavah.portal.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServices {
    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;
    private final UserMapper userMapper;
    private final R2Service r2Service;
    private final UserServices userServices;


    @Value("${cloudflare.r2.bucket-resources}")
    String bucketName;
    @Value("${cloudflare.r2.bucket-resources-url}")
    String bucketUrl;

    public ResourceDto uploadLesson(MultipartFile file, String title) {

        try {
            var address = this.r2Service.uploadFile(bucketName, bucketUrl, file);
            var curUser = this.userServices.currentUser();
            var resource = resourceMapper.toEntity(title);
            resource.setAddress(address);
            resource.setPostedBy(curUser);
            resource.setDivision((curUser.getDivision()).toLowerCase());
            resourceRepository.save(resource);
            var resourceDto = resourceMapper.toDto(resource);
            resourceDto.setUser(this.userMapper.userDto(curUser));
            return resourceDto;

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public List<ResourceDto> getResources() {
        var user = this.userServices.currentUser();
        var resources = resourceRepository.getResourcesByDivision((user.getDivision()).toLowerCase());


        return resourceMapper.toDtos(resources);
    }

    public String deleteLesson(Long id) {
        var curUser = userServices.currentUser();
        var resource = resourceRepository.findById(id).orElse(null);
        if (resource == null) {
            return "Resource not found";
        }
        if(!(curUser.getDivision().toLowerCase()).equals(resource.getDivision())) {
            return "You are not authorized to delete this resource";
        }
        resourceRepository.delete(resource);
        return "Resource deleted successfully";
    }



}
