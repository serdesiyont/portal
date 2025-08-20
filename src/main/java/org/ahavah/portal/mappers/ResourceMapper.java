package org.ahavah.portal.mappers;

import org.ahavah.portal.dtos.resource.ResourceDto;
import org.ahavah.portal.entities.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResourceMapper {
    @Mapping(target="postedBy", ignore = true)
    Resource toEntity(String title);

    ResourceDto toDto(Resource resource);

}
