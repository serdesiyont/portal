package org.ahavah.portal.contollers;

import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.ChatRequest;

import org.ahavah.portal.services.ConversationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;



    @PostMapping
    public String chat(
                             @RequestBody ChatRequest request){
        return conversationService.handleChat(request);
    }
}
