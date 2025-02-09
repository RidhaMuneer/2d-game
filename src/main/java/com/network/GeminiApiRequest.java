package com.network;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class GeminiApiRequest {
    private static final String API_KEY = "YOUR_KEY";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent";
    private static final Gson gson = new Gson();

    private List<Map<String, Object>> conversationHistory = new ArrayList<>();

    /**
     * Send a request to the Gemini API to generate a response based on the given user input. The request will include the current conversation history.
     * 
     * @param userInput the text that the user has entered
     * @return the response from the Gemini API, or "No response found" if there was an error
     * @throws IOException if there was an error communicating with the Gemini API
     * @throws InterruptedException if the thread was interrupted while waiting for the response
     */
    @SuppressWarnings("unchecked")
    public String sendRequest(String userInput) throws IOException, InterruptedException {
        // Create user message
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        
        Map<String, String> userPart = new HashMap<>();
        userPart.put("text", userInput);
        userMessage.put("parts", List.of(userPart));
        
        conversationHistory.add(userMessage);

        // Prepare request payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", conversationHistory);

        // System instruction
        Map<String, Object> systemInstruction = new HashMap<>();
        systemInstruction.put("role", "user");
        Map<String, String> systemPart = new HashMap<>();
        systemPart.put("text", "act like Socrates the philosopher, start by asking a question and then give me two options, whatever I choose, you ask another question accordingly with another two options, and so on. Type (A) before the first options and (B) before the second option. Also don't ask long questions please.");
        systemInstruction.put("parts", List.of(systemPart));
        payload.put("systemInstruction", systemInstruction);

        // Generation config
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 1.0);
        generationConfig.put("topK", 40);
        generationConfig.put("topP", 0.95);
        generationConfig.put("maxOutputTokens", 8192);
        generationConfig.put("responseMimeType", "text/plain");
        payload.put("generationConfig", generationConfig);

        // Convert to JSON
        String jsonPayload = gson.toJson(payload);

        // Send request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL + "?key=" + API_KEY))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Parse the JSON response
        Map<String, Object> responseMap = gson.fromJson(response.body(), Map.class);
        
        // Extract the text from the first candidate's content
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");
        if (candidates != null && !candidates.isEmpty()) {
            Map<String, Object> firstCandidate = candidates.get(0);
            Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            
            if (parts != null && !parts.isEmpty()) {
                return (String) parts.get(0).get("text");
            }
        }
        
        return "No response found";
    }
}