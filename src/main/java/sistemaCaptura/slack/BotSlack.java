package sistemaCaptura.slack;

import org.json.JSONObject;
import sistemaCaptura.Maquina;
import java.io.IOException;

public class BotSlack {


    public void mensagemHardware(String componente) throws IOException, InterruptedException {

        String mensagemSlack = "O componente " + componente + " atingiu/ultrapassou o limite estabelecido pelo ADM";

        JSONObject json = new JSONObject();

        json.put("text",mensagemSlack) ;

        ConfBotSlack.sendMessage(json);
    }


    public void mensagemSoftware(String processo,Maquina maquina) throws IOException, InterruptedException {

        String mensagemSlack = "("+maquina.getNome()+ ") esta sendo utilizado de maneira indevida um dos processo que estava sendo utilizando: "+processo+ " que é marcado como um dos processo não permitido";

        JSONObject json = new JSONObject();

        json.put("text",mensagemSlack) ;

        ConfBotSlack.sendMessage(json);
    }
}
