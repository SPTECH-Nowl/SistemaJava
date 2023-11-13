package sistemaCaptura;

import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import sistemaCaptura.conexao.Conexao;
import sistemaCaptura.slack.BotSlack;

import java.io.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HistConsmRecurso {

    private Integer idHistorico;
    private LocalDateTime dataHora = LocalDateTime.now();
    private Integer fkMaquina;
    private Integer fkHardware;
    private Integer fkComponente;

    Conexao conexao = new Conexao();
    JdbcTemplate con = conexao.getConexaoDoBanco();
    Looca looca = new Looca();
    Timer timer = new Timer();
    Timer timer02 = new Timer();

    BotSlack botSlack = new BotSlack();

    public HistConsmRecurso() {
    }

    public void mostrarHistorico(Integer maquinaId, String nomeAula) {

        insertHistorico(maquinaId, nomeAula);
    }

    public void fecharSistema() {
        System.out.println("Sistema encerrado. Até mais!");
        System.exit(0);
    }

    public void insertHistorico(Integer maquinaId, String nomeAula) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                double usoDouble = looca.getProcessador().getUso();
                int consumoCpu = (int) Math.round(usoDouble);
                long consumoRam = looca.getMemoria().getEmUso();
                long consumoDisco = looca.getGrupoDeDiscos().getTamanhoTotal();
                Integer qtdJanelasAbertas = looca.getGrupoDeJanelas().getTotalJanelas();
                dataHora = LocalDateTime.now();

                List<Componente> componentes = con.query("SELECT * FROM componente WHERE fkMaquina = ?",
                        new BeanPropertyRowMapper<>(Componente.class), maquinaId);

                insertDadosNoBanco(componentes.get(0).getIdComponente(), consumoCpu, maquinaId, componentes.get(0).getFkHardware());
                insertDadosNoBanco(componentes.get(1).getIdComponente(), consumoRam, maquinaId, componentes.get(1).getFkHardware());
                insertDadosNoBanco(componentes.get(2).getIdComponente(), consumoDisco, maquinaId, componentes.get(2).getFkHardware());


                List<Hardware> hardwares = con.query("SELECT * FROM hardware ",
                        new BeanPropertyRowMapper<>(Hardware.class));

                mostrarDadosEmTabela(consumoCpu, consumoRam, consumoDisco, qtdJanelasAbertas, hardwares.get(1), hardwares.get(2));

                GravarEmArquivo(dataHora, consumoCpu, consumoRam, consumoDisco, qtdJanelasAbertas);

                verificarLimiteEEnviarNotificacao("CPU", consumoCpu, componentes.get(0).getMax(), hardwares.get(0));
                verificarLimiteEEnviarNotificacao("RAM", consumoRam, componentes.get(1).getMax(), hardwares.get(1));
                verificarLimiteEEnviarNotificacao("Disco", consumoDisco, componentes.get(2).getMax(), hardwares.get(2));
                verificarLimiteEEnviarNotificacao("Quantidade janelas", qtdJanelasAbertas, 15, hardwares.get(0));

                MonitorarSoftware(maquinaId, nomeAula);


                // Chamar a função para criar e gravar no arquivo
//                GravarEmArquivo(dataHoraFormatada, consumoCpu, consumoRam, consumoDisco, qtdJanelasAbertas, sistemaOperacional, idMaquina);
            }
        }, 1000, 1000);
    }


    private void GravarEmArquivo(LocalDateTime dataHora, int consumoCpu, long consumoRam, long consumoDisco, Integer qtdJanelasAbertas) {
        String nomeDoArquivo = "C:\\Users\\Aluno\\IdeaProjects\\SistemaJava\\arquivo";

        // Mensagem para solicitar suporte
        String mensagemSuporte = "Suporte foi solicitado para arrumar a máquina com ID"; // deixa essa parte dinamica, para arrumar as maquinas que esta em alerta

        try {
            File arquivo = new File(nomeDoArquivo);

            if (!arquivo.exists()) {
                arquivo.createNewFile(); // arrumar a parte do log
            }

            BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivo, true));

            // Construir a string de dados
            String dados = "Data/Hora: " + dataHora + "\n" +
                    "Consumo CPU: " + consumoCpu + "%\n" +
                    "Consumo RAM: " + consumoRam + " bytes\n" +
                    "Consumo Disco: " + consumoDisco + " GB\n" +
                    "Janelas Abertas: " + qtdJanelasAbertas + " janelas abertas\n" +
                    "Mensagem para Suporte: " + mensagemSuporte + "\n\n";

            // Escrever os dados no arquivo
            escritor.write(dados);

            escritor.close();

            System.out.println("Dados gravados em " + nomeDoArquivo + ", Gerando LOG de consumos dos dados");

        } catch (IOException e) {
            e.printStackTrace();
        }
     }

    private void insertDadosNoBanco(Integer componente, Number consumo, Integer numeroMaquina, Integer tipohardware) {

        String sql = "INSERT INTO historico (dataHora, consumo,fkMaquina , fkHardware,fkComponente ) VALUES(?, ?, ?, ?, ?)";
        con.update(sql, dataHora, consumo, numeroMaquina, tipohardware, componente);
    }

    private void mostrarDadosEmTabela(int consumoCpu, long consumoRam, long consumoDisco, int qtdJanelasAbertas, Hardware hardware1, Hardware hardware2) {


        double Ram = 0.0;
        double bytes = consumoRam / 8.00;
        double gigabytes = bytes / (1024.0 * 1024 * 1024);
        double valorNormalizado = gigabytes / hardware1.getCapacidade();
        Ram = valorNormalizado * 100;
        double Disco = 0.0;
        double bytes2 = consumoDisco / 8.00;
        double gigabytes2 = bytes2 / (1024.0 * 1024 * 1024);
        double valorNormalizado2 = gigabytes2 / hardware2.getCapacidade();
        Disco = valorNormalizado2 * 100;


        System.out.println("| Data/Hora           | Consumo CPU  | Consumo RAM    | Consumo Disco | Janelas Abertas |");
        System.out.println("+---------------------+--------------+-----------------+-------------+----------------------+");
        System.out.print("| " + dataHora + " | " + consumoCpu + "%        | ");
        if (Ram > 0) {
            System.out.print(String.format(" %.2f%%  |  ", Ram));
        } else {
            System.out.println("N/A             | ");
        }
        if (Disco > 0) {
            System.out.print(String.format(" %.2f%%  |  ", Disco));
        } else {
            System.out.println("N/A             | ");
        }
        System.out.println(qtdJanelasAbertas + " janelas abertas |");
        System.out.println("+---------------------+--------------+-----------------+-------------+----------------------+");
    }

    private void verificarLimiteEEnviarNotificacao(String componente, long consumo, int limite, Hardware hardware) {
        double porcentagem = 0.0;
        if (componente.equals("RAM")) {
            double bytes = consumo / 8.00;
            double gigabytes = bytes / (1024.0 * 1024 * 1024);
            double valorNormalizado = gigabytes / hardware.getCapacidade();
            porcentagem = valorNormalizado;
        } else if (componente.equals("DISCO")) {
            double bytes = consumo / 8.00;
            double gigabytes = bytes / (1024.0 * 1024 * 1024);
            double valorNormalizado = gigabytes / hardware.getCapacidade();
            porcentagem = valorNormalizado;
        } else {
            porcentagem = consumo;
        }

        if (porcentagem >= limite) {

            if (componente.equals("CPU") || componente.equals("Janelas Abertas")) {
                // Notificar o usuário no Java
                System.out.println("alerta de limite no Jar");
            }

            // Enviar notificação por Slack
            try {
                botSlack.mensagemHardware(componente);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void MonitorarSoftware(Integer idMaquina, String nomeAula) {

        timer02.schedule(new TimerTask() {
            @Override
            public void run() {

                try {
                    ProcessBuilder processBuilder;
                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        processBuilder = new ProcessBuilder("tasklist");
                    } else {
                        processBuilder = new ProcessBuilder("ps", "aux");
                    }


                    List<Maquina.Processo> processos = con.query(" select idProcesso,nomeProcesso,nomeAplicativo from processo join permissaoProcesso on idprocesso = fkProcesso where fkPermissao=(select  idPermissao from permissao where nome = ?);",
                            new BeanPropertyRowMapper<>(Maquina.Processo.class), nomeAula);

                    List<Maquina> maquinas = con.query("SELECT * FROM maquina where idMaquina = ?",
                            new BeanPropertyRowMapper<>(Maquina.class), idMaquina);


                    processBuilder.redirectErrorStream(true);
                    Process process = processBuilder.start();

                    BufferedReader Busca = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String linhaBusca;


                    while (Busca.readLine() != null) {
                        linhaBusca = Busca.readLine();
                        for (Maquina.Processo processo : processos) {
                            if (linhaBusca.contains(processo.getNomeAplicativo()) && linhaBusca != null) {
                                dataHora = LocalDateTime.now();

                                con.update("INSERT INTO strike (dataHora, validade, motivo, duracao, fkMaquina, fkSituacao) VALUES (?, ?, ?, ?, ?, ?);", dataHora, 1, null, 30, idMaquina, 1);
                                botSlack.mensagemSoftware(processo.getNomeAplicativo(), maquinas.get(0));
                            }
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 5000);
    }
}
