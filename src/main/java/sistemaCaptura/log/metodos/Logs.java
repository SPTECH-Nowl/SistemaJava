package sistemaCaptura.log.metodos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Logs {
    private static final String CAMINHO_ARQUIVO = "src/main/java/sistemaCaptura/log/users/";

    public static void gerarLog(Integer idMaquina,Long consumoCpu,Long consumoRam,Long consumoDisco) {
        // Obter a data atual
        Date dataAtual = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String nomeArquivo = dateFormat.format(dataAtual) + "_log.txt";
        String caminhoCompleto = CAMINHO_ARQUIVO + nomeArquivo;

        try {
            if (Files.exists(Path.of(caminhoCompleto))) {
                // Se o arquivo já existe, adiciona mensagens
                adicionarMensagens(caminhoCompleto, dataAtual,idMaquina,consumoCpu,consumoRam,consumoDisco);
            } else {
                // Se o arquivo não existe, cria um novo e adiciona mensagens
                criarNovoArquivo(caminhoCompleto, dataAtual);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void adicionarMensagens(String caminhoCompleto, Date dataAtual,Integer idMaquina,Long consumoCpu,Long consumoRam,Long consumoDisco) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(caminhoCompleto), StandardOpenOption.APPEND)) {

            String mensagemSuporte = "Suporte foi solicitado para arrumar a máquina com ID " + idMaquina + ".";
            String dados = "Data/Hora: " + dataAtual + "\n" +
                    "ID da Máquina: " + idMaquina + "\n" +

                    "Consumo CPU: " + consumoCpu + "%\n" +
                    "Consumo RAM: " + consumoRam + " bytes\n" +
                    "Consumo Disco: " + consumoDisco + " GB\n" +
                    "Mensagem para Suporte: " + mensagemSuporte + "\n\n";

            // Adicionar mensagens ao arquivo existente
            writer.write(dados);
            writer.write("Nova mensagem de log adicionada.\n");
            System.out.println("Mensagem adicionada ao log em: " + caminhoCompleto);
        }
    }

    private static void criarNovoArquivo(String caminhoCompleto, Date dataAtual) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoCompleto))) {
            // Criar um novo arquivo com mensagens
            writer.write("Data/Hora: " + dataAtual.toString() + "\n");
            writer.write("Mensagem de log: Este é um exemplo de log.\n");
            System.out.println("Novo log gerado com sucesso em: " + caminhoCompleto);
        }
    }
}
