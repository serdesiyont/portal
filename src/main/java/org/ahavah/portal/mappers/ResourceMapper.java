package org.ahavah.portal.mappers;

import org.ahavah.portal.dtos.resource.ResourceDto;
import org.ahavah.portal.entities.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ResourceMapper {


    @Mapping(target="postedBy", ignore = true)
    Resource toEntity(String title);

    @Mapping(source = "postedBy", target = "user")
    ResourceDto toDto(Resource resource);

    List<ResourceDto> toDtos(List<Resource> resources);

}
