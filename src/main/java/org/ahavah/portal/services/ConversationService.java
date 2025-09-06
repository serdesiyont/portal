package org.ahavah.portal.services;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.ChatRequest;
import org.ahavah.portal.entities.Conversation;
import org.ahavah.portal.repositories.ConversationRepository;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final UserServices userServices;


   public String handleChat(ChatRequest chatRequest) {
       var user =  this.userServices.currentUser();
       var apiKey = user.getApiKey();
       if(apiKey == null) {
           throw new IllegalArgumentException("apiKey is null");
       }

        var chatHistory = this.conversationRepository.findByUserOrderByCreatedAtDesc(user, Limit.of(5));



        GenerateContentResponse response = chat(chatRequest.getMessage(),apiKey, formatHistory(chatHistory));

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

        String SYSTEM_INSTRUCTION = "You are a supportive AI programming tutor. Your goal is to help students learn programming concepts with clarity, patience, and encouragement.\n" + //
                        "\n" + //
                        "- Explain concepts step by step in a way that builds understanding and intuition  \n" + //
                        "- Provide runnable code examples only when they are genuinely helpful.  \n" + //
                        "- Use GitHub-flavored Markdown for formatting:\n" + //
                        "  - Use `#` for main heading and use`##` subsections  when the topic needs detailed structure.  \n" + //
                        "  - Use fenced code blocks (```language) if the topic needs code examples  \n" + //
                        "  - Use lists or tables only if they make the explanation easier to follow.  \n" + //
                        "- Keep answers adaptive: short and direct for simple questions, structured and detailed only when needed.  \n" + //
                        "- Encourage exploration, highlight common mistakes, and ask clarifying questions when it helps guide the student.  \n";//
                        
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
            
            String MODEL_NAME = "gemini-2.0-flash";

            reply = client.models.generateContent(
                    MODEL_NAME,
                    fullPrompt,
                    null
            );
        }
        System.out.println(reply);
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
