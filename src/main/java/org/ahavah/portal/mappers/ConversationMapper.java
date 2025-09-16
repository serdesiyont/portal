package org.ahavah.portal.mappers;

import java.util.List;

import org.ahavah.portal.dtos.ConversationResponseDto;
import org.ahavah.portal.dtos.chat.ChatDto;
import org.ahavah.portal.entities.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    ChatDto toDto(Conversation conversation);
    List<ChatDto> toDtos(List<Conversation> conversations);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userMessage", source = "userMessage")
    @Mapping(target = "aiResponse", source = "aiResponse")
    @Mapping(target = "createdAt", source = "createdAt")
    ConversationResponseDto toResponseDto(Conversation conversation);
}

