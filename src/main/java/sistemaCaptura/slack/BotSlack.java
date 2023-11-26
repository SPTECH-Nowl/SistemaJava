package sistemaCaptura.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;

public class BotSlack {

    private final MethodsClient client;
    private final String slackToken;
    private final String channel;

    public BotSlack(String slackToken, String channel) {
        this.client = Slack.getInstance().methods();
        this.slackToken = slackToken;
        this.channel = channel;
    }

    public void enviarMensagem(String mensagem) {
        try {
            var resultado = client.chatPostMessage(r -> r
                    .token(slackToken)
                    .channel(channel)
                    .text(mensagem)
            );
        } catch (SlackApiException | IOException erro) {
            System.err.println("Erro ao enviar mensagem para o Slack: " + erro.getMessage());
        }
    }
}
