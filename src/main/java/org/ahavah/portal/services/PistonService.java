package org.ahavah.portal.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ahavah.portal.dtos.exercise.ExerciseDto;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;


@Service
public class PistonService {

    @Value("${piston.base-url}")
    String baseUrl;

    public String executeCode(ExerciseDto exerciseDto) throws HttpException, URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        // Prepare file name, code, and stdin
        String fileName = exerciseDto.getBoilerplate().getOrDefault("fileName", "test.py").toString();
        String code = exerciseDto.getBoilerplate().get("code").toString();
        String stdin = exerciseDto.getBoilerplate().getOrDefault("input", "").toString();
        String language = exerciseDto.getLanguage();
        String args = exerciseDto.getBoilerplate().getOrDefault("args", "").toString();
        Map<String, String> versions = new HashMap<>();
        versions.put("python", "3.10.0");
        versions.put("typescript",  "5.0.3");
        versions.put("javascript",  "18.15.0");
        versions.put("java", "15.0.2");
        versions.put("go", "1.16.2");
        versions.put("rust", "1.68.2" );
        versions.put("cpp",  "10.2.0");



        // Build payload as a map
        Map<String, Object> file = new HashMap<>();
        file.put("name", fileName);
        file.put("content", code);
        Map<String, Object> payload = new HashMap<>();
        payload.put("files", java.util.List.of(file));
        payload.put("language", language);
        payload.put("stdin", stdin);
        payload.put("version", versions.getOrDefault(language.toLowerCase(), ""));
        payload.put("args", args);
        payload.put("compile_timeout", 10000);
        payload.put("run_timeout", 3000);
        payload.put("compile_memory_limit", -1);
        payload.put("run_memory_limit", -1);

        String requestBody = objectMapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new HttpException("Failed to execute code: " + response.body());
        }
        String res = response.body();
        JsonNode root = objectMapper.readTree(res);
        return root.path("run").path("output").asText();
    }
}
