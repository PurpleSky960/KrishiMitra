package microsoftAI.KrishiMitra.krishiApp.service.implementation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import microsoftAI.KrishiMitra.krishiApp.dto.CoordinateDTO;
import microsoftAI.KrishiMitra.krishiApp.dto.FarmRiskProfileDTO;
import microsoftAI.KrishiMitra.krishiApp.dto.RiskAnalysisRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RiskServiceImpl {

    @Value("${agromonitoring.api.key}")
    private String agroApiKey;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public FarmRiskProfileDTO generateRiskProfile(RiskAnalysisRequestDTO request) {
        try {
            // STEP 1: Create Polygon in Agromonitoring
            String polyId = createAgroPolygon(request.getCoordinates());

            // STEP 2: Fetch Weather & Soil Data
            String weatherData = restTemplate.getForObject(
                    "http://api.agromonitoring.com/agro/1.0/weather?polyid=" + polyId + "&appid=" + agroApiKey, String.class);
            String soilData = restTemplate.getForObject(
                    "http://api.agromonitoring.com/agro/1.0/soil?polyid=" + polyId + "&appid=" + agroApiKey, String.class);

            // STEP 3: Pass Data to Gemini API
            return askGeminiForInsights(weatherData, soilData);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to analyze farm risk via external APIs.", e);
        }
    }

    private String createAgroPolygon(List<CoordinateDTO> coords) throws Exception {
        StringBuilder geoJsonCoords = new StringBuilder("[[");
        for (CoordinateDTO c : coords) {
            geoJsonCoords.append("[").append(c.getLng()).append(",").append(c.getLat()).append("],");
        }
        geoJsonCoords.append("[").append(coords.get(0).getLng()).append(",").append(coords.get(0).getLat()).append("]]]");

        String payload = """
            {
                "name":"KrishiMitra_Scan",
                "geo_json":{
                    "type":"Feature",
                    "properties":{},
                    "geometry":{
                        "type":"Polygon",
                        "coordinates": %s
                    }
                }
            }
        """.formatted(geoJsonCoords.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        String response = restTemplate.postForObject(
                "http://api.agromonitoring.com/agro/1.0/polygons?appid=" + agroApiKey, entity, String.class);

        // GSON PARSING INSTEAD OF JACKSON
        JsonObject root = JsonParser.parseString(response).getAsJsonObject();
        return root.get("id").getAsString();
    }

    private FarmRiskProfileDTO askGeminiForInsights(String weatherData, String soilData) throws Exception {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey;

        String prompt = "Act as an expert agronomist. Analyze this raw satellite data. Weather: " + weatherData + " Soil: " + soilData +
                ". Return ONLY a valid JSON object with the following exact keys and types: overallRiskScore (int 0-100), cropScore (int), weatherScore (int), marketScore (int), pestScore (int), soilScore (int), immediateThreats (array of objects with 'title', 'severity' (HIGH/MEDIUM), 'description'), protectiveActions (array of objects with 'actionText'). No markdown, just pure JSON.";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        String response = restTemplate.postForObject(url, entity, String.class);

        // GSON PARSING INSTEAD OF JACKSON
        JsonObject root = JsonParser.parseString(response).getAsJsonObject();
        String generatedJsonText = root.getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();

        generatedJsonText = generatedJsonText.replace("```json\n", "").replace("\n```", "");

        Gson gson = new Gson();
        return gson.fromJson(generatedJsonText, FarmRiskProfileDTO.class);
    }}