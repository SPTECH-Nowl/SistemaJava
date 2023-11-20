package sistemaCaptura.log.metodos;

import sistemaCaptura.Maquina;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Logs {
    private static final String CAMINHO_ARQUIVO = "src/main/java/sistemaCaptura/log/users/";
    private static final int LIMITE_CPU = 60;  // Defina o limite máximo de CPU conforme necessário
    private static final int LIMITE_RAM = 60;  // Defina o limite máximo de RAM conforme necessário

    public static void gerarLog(Maquina maquina, Long consumoCpu, double consumoRam, double consumoDisco) {
        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);
        String nomeArquivo = dateFormat.format(dataAtual) + "_log.txt";
        String caminhoCompleto = CAMINHO_ARQUIVO + nomeArquivo;

        if (Files.exists(Path.of(caminhoCompleto))) {
            adicionarMensagens(caminhoCompleto, dataAtual, maquina, consumoCpu, consumoRam, consumoDisco);
        } else {
            criarNovoArquivo(caminhoCompleto, dataAtual);
        }
    }

    private static void adicionarMensagens(String caminhoCompleto, LocalDate dataAtual, Maquina maquina, Long consumoCpu, double consumoRam, double consumoDisco) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(caminhoCompleto), StandardOpenOption.APPEND)) {

            String mensagemSuporte = "Suporte foi solicitado para arrumar  a maquina (" + maquina.getNome() + ").";

            // Adicionar mensagem relacionada ao consumo máximo de CPU e RAM
            String mensagemConsumo = String.format("O consumo de CPU estourou o máximo sugerido (%d%%). O consumo de RAM atingiu o máximo sugerido (%d%%) de acordo com o nome da máquina.%n", LIMITE_CPU, LIMITE_RAM);

            String dados = String.format("Data/Hora: %s%nNome da Máquina: %s%nConsumo CPU: %d%%%nConsumo RAM: %.2f bytes%nConsumo Disco: %.2f GB%nMensagem para Suporte: %s%n%s%n",
                    dataAtual, maquina.getNome(), consumoCpu, consumoRam, consumoDisco, mensagemSuporte, mensagemConsumo);

            writer.write(dados);
//            System.out.println("Mensagem adicionada ao log em: " + caminhoCompleto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void criarNovoArquivo(String caminhoCompleto, LocalDate dataAtual) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoCompleto))) {
            System.out.println("log gerado com sucesso em: " + caminhoCompleto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void adicionarMotivo(String mensagem){

        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);
        String nomeArquivo = dateFormat.format(dataAtual) + "_log.txt";
        String caminhoCompleto = CAMINHO_ARQUIVO + nomeArquivo;


        if (Files.exists(Path.of(caminhoCompleto))) {

            LocalDateTime dataAtualLogs = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);


            // Formata a data de acordo com o formato especificado
            String dataFormatadaLog = formatter.format(dataAtualLogs);

            String mesagemLog =dataFormatadaLog+mensagem;

            salvarMensagem(caminhoCompleto,mesagemLog);
        } else {
            criarNovoArquivo(caminhoCompleto, dataAtual);
        }
    }

    private static void salvarMensagem(String caminhoCompleto,String mensagem ){
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(caminhoCompleto), StandardOpenOption.APPEND)) {
            writer.write(mensagem+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}