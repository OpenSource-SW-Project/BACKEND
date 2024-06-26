package Opensource_SW_Project.Project.service.ModerationService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

@Service
public class ModerationServicelmpl implements ModerationService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.moderation.url}")
    private String apiUrl;

    public String getChatCompletion(String prompt) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(apiUrl);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + apiKey);

        String requestBody = "{ \"input\": \"" + prompt + "\" }";
        httpPost.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));

        HttpResponse response = httpClient.execute(httpPost);
        String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        httpClient.close();

        if (response.getStatusLine().getStatusCode() != 200) {
            return String.format("Failed with HTTP error code : %d, response : %s",
                    response.getStatusLine().getStatusCode(), responseString);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseString);

        String result = checkModeration(jsonNode);

        //System.out.println("Moderation Result:" + result);

        return result;
    }

    public String checkModeration (JsonNode response) {
        try {
            JsonNode resultsNode = response.path("results").get(0);
            JsonNode categoryScoresNode = resultsNode.path("category_scores");

            String[] categoryScores = new String[categoryScoresNode.size()];
            String result = null;
            double threshold = 0.015;

            int index = 0;
            Iterator<Map.Entry<String, JsonNode>> fields = categoryScoresNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();

                double value = field.getValue().asDouble();
                if (field.getValue().asDouble() >= threshold) {
                    result = field.getKey();
                    threshold = field.getValue().asDouble();
                    System.out.println("Moderation Detect:" + field.getKey() + ": " + field.getValue().toString());
                }

                categoryScores[index++] = field.getKey() + ": " + field.getValue().toString();
            }

            // Print the results
            for (String score : categoryScores) {
                //System.out.println(score);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}