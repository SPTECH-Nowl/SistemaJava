package sistemaCaptura.slack;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class ConfBotSlack {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String URL="https://hooks.slack.com/services/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";
//A URL NÂO VAI FUNCIONAR DEPOIS DE COLOCAR NO GITHUB TROQUE ELA ANTES DE UTILIZAR O CHATBOT
//    JÀ ESTOU MONTANDO UM JEITO DE ARRUMAR ISSO DA URL
    public static void sendMessage(JSONObject content) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(URL))
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(content.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 || response.statusCode()!=429) {
            System.out.println(String.format("Status: %s", response.statusCode()));
            System.out.println(String.format("Response: %s", response.body()));
        }

    }
}

