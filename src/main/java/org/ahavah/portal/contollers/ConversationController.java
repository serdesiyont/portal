package org.ahavah.portal.contollers;

import org.ahavah.portal.dtos.ConversationResponseDto;
import org.ahavah.portal.dtos.chat.ChatRequest;
import org.ahavah.portal.services.ConversationService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;

    

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody ChatRequest request) {
        return ResponseEntity.ok(conversationService.handleChat(request));
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public  SseEmitter handleChatStream(@RequestBody ChatRequest chatRequest) {
        SseEmitter emitter = new SseEmitter(-1L); // Use -1 for no timeout
        conversationService.handleChatStream(chatRequest, emitter);
        return (emitter);
    }

    @GetMapping
    public ResponseEntity<Page<ConversationResponseDto>> getMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(this.conversationService.getPreviousConvo(page, size));
    }
}
