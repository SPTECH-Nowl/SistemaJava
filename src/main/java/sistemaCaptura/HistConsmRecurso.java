package sistemaCaptura;

import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import sistemaCaptura.conexao.Conexao;
import sistemaCaptura.log.metodos.Logs;
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
    Logs logs = new Logs();

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
                MonitorarSoftware(maquinaId, nomeAula);

                insertDadosNoBanco(componentes.get(0).getIdComponente(), consumoCpu, maquinaId, componentes.get(0).getFkHardware());
                insertDadosNoBanco(componentes.get(1).getIdComponente(), consumoRam, maquinaId, componentes.get(1).getFkHardware());
                insertDadosNoBanco(componentes.get(2).getIdComponente(), consumoDisco, maquinaId, componentes.get(2).getFkHardware());


                List<Hardware> hardwares = con.query("SELECT * FROM hardware ",
                        new BeanPropertyRowMapper<>(Hardware.class));

                mostrarDadosEmTabela(consumoCpu, consumoRam, consumoDisco, qtdJanelasAbertas, hardwares.get(1), hardwares.get(2));


                try {
                    verificarLimiteEEnviarNotificacao("CPU", consumoCpu, componentes.get(0).getMax(), hardwares.get(0));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    verificarLimiteEEnviarNotificacao("RAM", consumoRam, componentes.get(1).getMax(), hardwares.get(1));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    verificarLimiteEEnviarNotificacao("Disco", consumoDisco, componentes.get(2).getMax(), hardwares.get(2));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    verificarLimiteEEnviarNotificacao("Quantidade janelas", qtdJanelasAbertas, 15, hardwares.get(0));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

Maquina maquina = obterMaquina(maquinaId);

                // Chamar a função para criar e gravar no arquivo
                logs.gerarLog(maquina, (long) consumoCpu, consumoRam, consumoDisco);
            }
        }, 1000, 10000);
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

    private void verificarLimiteEEnviarNotificacao(String componente, long consumo, int limite, Hardware hardware) throws IOException, InterruptedException {
        final Boolean[] timeoutAtivo = {false};
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
            if (!timeoutAtivo[0]) {
                timeoutAtivo[0] = true;
                botSlack.mensagemHardware(componente);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        timeoutAtivo[0] = false;
                    }
                }, 1000, 5000); // 5000 milissegundos = 5 segundos
            }
        }
    }


    public void MonitorarSoftware(Integer idMaquina, String nomeAula) {
        Timer timer = new Timer();
        final Boolean[] strike = {false};
        final String[] nomeUltimoProcesso = new String[1];
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String comando = (System.getProperty("os.name").toLowerCase().contains("win")) ? "tasklist" : "ps aux";

                    ProcessBuilder processBuilder = new ProcessBuilder(comando);
                    processBuilder.redirectErrorStream(true);
                    Process process = processBuilder.start();

                    BufferedReader busca = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String linhaBusca;

                    while ((linhaBusca = busca.readLine()) != null) {
                        for (Maquina.Processo processo : obterProcessos(nomeAula)) {
                            if (linhaBusca.contains(processo.getNomeAplicativo())) {
                               strike[0] = true;
                                System.out.println(processo.getNomeAplicativo());
                               nomeUltimoProcesso[0] = processo.getNomeAplicativo();
                            }
                        }
                    }
                    if (strike[0] == true){
                        LocalDateTime dataHora = LocalDateTime.now();
                        cadastrarStrike(idMaquina, dataHora);
                        botSlack.mensagemSoftware(nomeUltimoProcesso[0], obterMaquina(idMaquina));
                        System.out.println("Strike");
                        timer.cancel(); // Cancela o timer após cadastrar um "strike"
                    }else{
                        System.out.println("Você esta limpo amigo");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 5000, 15000); // Inicia após 5 segundos e repete a cada 15 segundos
    }

    private List<Maquina.Processo> obterProcessos(String nomeAula) {
        return con.query("SELECT idProcesso, nomeProcesso, nomeAplicativo FROM processo JOIN permissaoProcesso ON idprocesso = fkProcesso WHERE fkPermissao=(SELECT idPermissao FROM permissao WHERE nome = ?)",
                new BeanPropertyRowMapper<>(Maquina.Processo.class), nomeAula);
    }

    private Maquina obterMaquina(Integer idMaquina) {
        return con.queryForObject("SELECT * FROM maquina WHERE idMaquina = ?",
                new BeanPropertyRowMapper<>(Maquina.class), idMaquina);
    }

    private void cadastrarStrike(Integer idMaquina, LocalDateTime dataHora) {
        con.update("INSERT INTO strike (dataHora, validade, motivo, duracao, fkMaquina, fkSituacao) VALUES (?, ?, ?, ?, ?, ?);", dataHora, 1, "Uso indevido", 30, idMaquina, 1);
    }


}