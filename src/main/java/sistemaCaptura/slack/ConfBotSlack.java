package sistemaCaptura.slack;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class ConfBotSlack {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String URL = "https://hooks.slack.com/services/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";
//A URL NÂO VAI FUNCIONAR DEPOIS DE COLOCAR NO GITHUB TROQUE ELA ANTES DE UTILIZAR O CHATBOT
//    JÀ ESTOU MONTANDO UM JEITO DE ARRUMAR ISSO DA URL
    public static void sendMessage(JSONObject content) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(URL))
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(content.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());



    }
}

