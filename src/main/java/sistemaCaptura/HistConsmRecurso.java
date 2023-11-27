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
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HistConsmRecurso {

    private LocalDateTime dataHora = LocalDateTime.now();

    Conexao conexao = new Conexao();
    JdbcTemplate con = conexao.getConexaoDoBanco();
    Looca looca = new Looca();
    Timer timer = new Timer();
    Timer timer02 = new Timer();
    List<Hardware> hardwares;

    BotSlack botSlack = new BotSlack("xoxb-6077098544578-6249289926579-6x7cPWRKwGA860AQbKZ4vpiq", "C062962NFKM");
    Logs logs = new Logs();
    Integer qtdStrike = 0;

    public HistConsmRecurso() {
    }

    public void mostrarHistorico(Integer maquinaId, String nomeAula) {
        MonitorarSoftware(maquinaId, nomeAula);
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
                double consumoRam = looca.getMemoria().getEmUso().doubleValue();
                double consumoDisco = looca.getGrupoDeDiscos().getTamanhoTotal().doubleValue();
                Integer qtdJanelasAbertas = looca.getGrupoDeJanelas().getTotalJanelas();
                dataHora = LocalDateTime.now();


                List<Componente> componentes = con.query("SELECT * FROM componente WHERE fkMaquina = ?",
                        new BeanPropertyRowMapper<>(Componente.class), maquinaId);
                if (componentes.size() >= 3) {
                    String motivoComponentes = ":--SUCCESS: O sistema localizou os 3 componentes para ser monitorados)!";
                    logs.adicionarMotivo(motivoComponentes);

                    hardwares = con.query("SELECT * FROM hardware ",
                            new BeanPropertyRowMapper<>(Hardware.class));

                    double Ram = 0.0;
                    double bytes = consumoRam / 8.00;
                    double gigabytes = bytes / (1024.0 * 1024 * 1024);
                    double valorNormalizado = gigabytes / hardwares.get(1).getCapacidade();
                    Ram = valorNormalizado * 100;
                    double Disco = 0.0;
                    double bytes2 = consumoDisco / 8.00;
                    double gigabytes2 = bytes2 / (1024.0 * 1024 * 1024);
                    double valorNormalizado2 = gigabytes2 / hardwares.get(2).getCapacidade();
                    Disco = valorNormalizado2 * 100;
                    DecimalFormat df = new DecimalFormat("#.##");

                    // Formatando o número com duas casas decimais
                    double ramFormatada = Math.round(Ram * 100.0) / 100.0;
                    double discoFormatada = Math.round(Disco * 100.0) / 100.0;
                    insertDadosNoBanco(componentes.get(0).getIdComponente(), consumoCpu, maquinaId, componentes.get(0).getFkHardware());
                    insertDadosNoBanco(componentes.get(1).getIdComponente(), ramFormatada, maquinaId, componentes.get(1).getFkHardware());
                    insertDadosNoBanco(componentes.get(2).getIdComponente(), discoFormatada, maquinaId, componentes.get(2).getFkHardware());


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
                } else {
                    String motivoComponentes = ":--ERROR: a maquina com o ID:(" + maquinaId + ") não possui 3 componentes para ser monitorados)!";
                    logs.adicionarMotivo(motivoComponentes);
                    fecharSistema();
                }

            }
        }, 1000, 10000);
    }

    private void insertDadosNoBanco(Integer componente, Number consumo, Integer numeroMaquina, Integer tipohardware) {

        String sql = "INSERT INTO historico (dataHora, consumo,fkMaquina , fkHardware,fkComponente ) VALUES(?, ?, ?, ?, ?)";
        con.update(sql, dataHora, consumo, numeroMaquina, tipohardware, componente);
    }

    private void mostrarDadosEmTabela(int consumoCpu, double consumoRam, double consumoDisco, int qtdJanelasAbertas, Hardware hardware1, Hardware hardware2) {


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

    private void verificarLimiteEEnviarNotificacao(String componente, double consumo, int limite, Hardware hardware) throws IOException, InterruptedException {
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
                String mensagemSlack = "O componente " + componente + " atingiu/ultrapassou o limite estabelecido pelo ADM";
                botSlack.enviarMensagem(mensagemSlack);
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
                    if (qtdStrike <= 2) {
                        while ((linhaBusca = busca.readLine()) != null) {
                            for (Maquina.Processo processo : obterProcessos(nomeAula)) {
                                if (linhaBusca.contains(processo.getNomeAplicativo())) {
                                    Maquina maquina = obterMaquina(idMaquina);

                                    strike[0] = true;
                                    System.out.println("Achou algo que nao devia");
                                    nomeUltimoProcesso[0] = processo.getNomeAplicativo();

                                    String mensagemSlack = "ALERT -- A maquina (" + maquina.getNome() + ") esta sendo utilizado de maneira indevida um dos processo que estava sendo utilizando: " + nomeUltimoProcesso[0] + " que é marcado como um dos processo não permitido";
                                    System.out.println("Deu strike");
                                    botSlack.enviarMensagem(mensagemSlack);
                                    qtdStrike++;
                                    if (qtdStrike == 2) {
                                        mensagemSlack = "ALERT -- A maquina (" + maquina.getNome() + ") esta com 3 strikes cadastrados desde o começo da operação do sistema: essa mensagem sera enviada pelo slack e o aluno responsavel pela maquina será notificado. ";

                                        botSlack.enviarMensagem(mensagemSlack);
                                    }
                                    LocalDateTime dataHora = LocalDateTime.now();
                                    cadastrarStrike(idMaquina, dataHora);
                                    return;
                                }
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0000, 15000); // Inicia após 5 segundos e repete a cada 15 segundos
    }

    private List<Maquina.Processo> obterProcessos(String nomeAula) {
        return con.query("SELECT idProcesso, nomeProcesso, nomeAplicativo FROM processo INNER JOIN permissaoProcesso ON idprocesso = fkProcesso WHERE fkPermissao=(SELECT idPermissao FROM permissao WHERE nome = ?)",
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