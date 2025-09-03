package org.ahavah.portal.services;

import com.google.genai.Client;
import com.google.genai.types.Candidate;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.ChatRequest;
import org.ahavah.portal.dtos.ChatResponse;
import org.ahavah.portal.entities.Conversation;
import org.ahavah.portal.repositories.ConversationRepository;
import org.checkerframework.checker.index.qual.GTENegativeOne;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final UserServices userServices;

    private static final double MODEL_TEMPERATURE = 0.4d; // not directly applied (API lacks config hook in this version)
    private static final double MODEL_TOP_P = 0.95d;       // kept for future use
    private static final int MODEL_MAX_OUTPUT_TOKENS = 1024; // kept for future use

    private static final String SYSTEM_INSTRUCTION = "You are an AI programming tutor helping a student learn programming. " +
            "Provide clear, step-by-step explanations, encourage best practices, and when appropriate ask concise clarifying " +
            "questions before assuming requirements. Be encouraging, avoid overwhelming the student, and prefer runnable code examples. " +
            "Always format responses using GitHub-flavored Markdown: use '# ' for the main title (only one), '## ' for subsections, fenced code blocks with language hints (e.g., ```java), bullet/numbered lists where helpful, tables when they add clarity, and inline links as [text](url). Start with a short '# Summary' section, then details in logically ordered '##' sections. Include a '## Explanation' and when code is present a '## Code' and '## Next Steps' section. Keep code minimal, correct, and runnable.";

   public String handleChat(ChatRequest chatRequest) {
       Map<String, String> history =  new HashMap<>();
       var user =  this.userServices.currentUser();
       // apiKey handling intentionally untouched per instructions

        var chatHistory = this.conversationRepository.findByUserOrderByCreatedAtDesc(user, Limit.of(10));
        for (Conversation entry : chatHistory) {
            history.put(entry.getUserMessage(), entry.getAiResponse());
        }

        String GOOGLE_API_KEY="AIzaSyDOEOWceY4g_UwZjZFcyFqDm3gt3i5g9cU"; // left as-is
        GenerateContentResponse response = chat(chatRequest.getMessage(),GOOGLE_API_KEY, formatHistory(chatHistory));

       String aiResponse = extractPlainText(response);

       // persist conversation
       Conversation conv = new Conversation();
       conv.setUser(user);
       conv.setUserMessage(chatRequest.getMessage());
       conv.setAiResponse(aiResponse);
       conv.setCreatedAt(OffsetDateTime.now());
       conversationRepository.save(conv);
       return aiResponse;

   }

    // Build a chronological history block (oldest -> newest)
    private String formatHistory(List<Conversation> recentDesc) {
        if (recentDesc == null || recentDesc.isEmpty()) return "";
        var builder = new StringBuilder();
        // recentDesc currently newest first; iterate reverse for chronological order
        for (int i = recentDesc.size() - 1; i >= 0; i--) {
            Conversation c = recentDesc.get(i);
            builder.append("User: ").append(c.getUserMessage()).append('\n');
            if (c.getAiResponse() != null && !c.getAiResponse().isBlank()) {
                builder.append("Tutor: ").append(c.getAiResponse()).append('\n');
            }
        }
        return builder.toString();
    }

    private GenerateContentResponse chat(String message, String apiKey, String chatHistory) {
        // Compose enriched prompt with system instruction + context + new user message
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("[SYSTEM]\n").append(SYSTEM_INSTRUCTION).append("\n\n");
        if (chatHistory != null && !chatHistory.isBlank()) {
            promptBuilder.append("[CONTEXT - recent conversation]\n").append(chatHistory).append("\n\n");
        }
        promptBuilder.append("[USER]\n").append(message).append("\n\n")
                .append("Please answer as a programming tutor. Format the entire response in GitHub-flavored Markdown with one top-level '# Summary' heading, subsequent '##' subsections, and fenced code blocks (```language). Provide links when referencing external docs. If you show code, add a brief explanation section.");

        String fullPrompt = promptBuilder.toString();

        GenerateContentResponse reply;
        try (Client client = Client.builder().apiKey(apiKey).build()) {
            // Library version (1.0.0) doesn't expose a documented generation config; using composed prompt only.
            // Model related constants (adjust as needed if advanced config becomes available)
            // unified model reference
            String MODEL_NAME = "gemini-2.0-flash";
            reply = client.models.generateContent(
                    MODEL_NAME,
                    fullPrompt,
                    null
            );
        }
        return reply;
    }


    private String extractPlainText(GenerateContentResponse response) {
     if (response == null) return "";
     // Fast path: use helper that aggregates first candidate text
     String aggregated = response.text();
     if (aggregated != null) return aggregated;

     // Fallback: concatenate all text parts from all candidates
     return response.candidates()
             .orElse(List.of())
             .stream()
             .map(c -> c.content().orElse(null))
             .filter(Objects::nonNull)
             .flatMap(content -> content.parts().orElse(List.of()).stream())
             .map(part -> part.text().orElse(""))
             .filter(s -> !s.isEmpty())
             .collect(Collectors.joining("\n"));
 }


}
