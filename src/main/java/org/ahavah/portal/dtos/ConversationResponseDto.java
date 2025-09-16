package org.ahavah.portal.dtos;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponseDto {
    private long id;
    private String userMessage;
    private String aiResponse;
    private OffsetDateTime createdAt;
}
