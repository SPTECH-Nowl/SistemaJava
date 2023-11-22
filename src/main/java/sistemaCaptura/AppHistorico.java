package sistemaCaptura;

import sistemaCaptura.conexao.Conexao;
import sistemaCaptura.log.metodos.Logs;
import sistemaCaptura.user.Usuario;
import sistemaCaptura.user.Adiministrador;
import sistemaCaptura.user.Professor;
import sistemaCaptura.user.AdmNowl;
import sistemaCaptura.user.Aluno;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class AppHistorico {

    static Logs logs = new Logs();
    static LocalDate dataAtual;
    static Conexao conexao = new Conexao();

    public static void main(String[] args) {
        JdbcTemplate con = conexao.getConexaoDoBanco();
        HistConsmRecurso histConsmRecurso = new HistConsmRecurso();

        Scanner in = new Scanner(System.in);
        Integer escolha;
        String motivoComeco = ":--SUCCESS: O Sistema iniciou normalmente";
        logs.adicionarMotivo(motivoComeco);
        do {
            System.out.println("-".repeat(15));
            System.out.println("Bem-vindo ao sistema Nowl");
            System.out.println("Escolha uma das opções abaixo");
            System.out.println("1 - Fazer login");
            System.out.println("2 - Sair");
            System.out.println("3 - Cadastrar máquina");
            System.out.println("-".repeat(15));

            escolha = in.nextInt();

            switch (escolha) {
                case 1:
                    fazerLogin(con, histConsmRecurso, in);
                    break;
                case 2:
                    exibirMensagemDespedida();
                    histConsmRecurso.fecharSistema();
                    break;
                case 3:
                    cadastrarMaquina(con);
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        } while (escolha != 2);
    }

    private static void cadastrarUsuario(JdbcTemplate con) {
        Scanner leitor = new Scanner(System.in);

        System.out.println("Digite o nome do usuário (mínimo de 3 caracteres):");
        String nome = leitor.nextLine();
        while (nome.length() < 3) {
            System.out.println("O nome deve ter no mínimo 3 caracteres. Tente novamente:");
            nome = leitor.nextLine();
        }

        System.out.println("Digite o email do usuário (deve conter '@'):");
        String email = leitor.nextLine();
        while (!email.contains("@")) {
            System.out.println("O email deve conter o caractere '@'. Tente novamente:");
            email = leitor.nextLine();
        }

        System.out.println("Digite a senha do usuário (mínimo de 8 caracteres):");
        String senha = leitor.nextLine();
        while (senha.length() < 8) {
            System.out.println("A senha deve ter no mínimo 8 caracteres. Tente novamente:");
            senha = leitor.nextLine();
        }

        System.out.println("Digite o ID da instituição (fkInstituicao):");
        int fkInstituicao = leitor.nextInt();

        System.out.println("Digite o ID do tipo de usuário (fkTipoUsuario) (deve ser 1, 2 ou 3):");
        int fkTipoUsuario = leitor.nextInt();
        while (fkTipoUsuario < 1 || fkTipoUsuario > 3) {
            System.out.println("O ID do tipo de usuário deve ser 1, 2 ou 3. Tente novamente:");
            fkTipoUsuario = leitor.nextInt();
        }

        // Outros campos de cadastro, se necessário

        // Inserir o novo usuário no banco de dados com as colunas fkInstituicao e fkTipoUsuario
        if (conexao.getDev()) {
            con.update("INSERT INTO usuario (nome, email, senha, fkInstituicao, fkTipoUsuario) VALUES (?, ?, ?, ?, ?)", nome, email, senha, fkInstituicao, fkTipoUsuario);
        } else {
            con.update("INSERT INTO usuario (nome, email, senha, fkInstituicao, fkTipoUsuario) VALUES (?, ?, ?, ?, ?)", nome, email, senha, fkInstituicao, fkTipoUsuario);
        }


        System.out.println("Usuário cadastrado com sucesso!");
    }

    private static void fazerLogin(JdbcTemplate con, HistConsmRecurso histConsmRecurso, Scanner in) {
        Scanner leitor = new Scanner(System.in);
        Integer numeroMaquina = null;
        Usuario usuario;

        System.out.println("Digite o seu email:");
        String email = leitor.nextLine();
        while (!email.contains("@")) {
            System.out.println("O email deve conter o caractere '@'. Tente novamente:");
            email = leitor.nextLine();
        }

        System.out.println("Digite a sua senha (mínimo de 8 caracteres):");
        String senha = leitor.nextLine();
        while (senha.length() < 8) {
            System.out.println("A senha deve ter no mínimo 8 caracteres. Tente novamente:");
            senha = leitor.nextLine();
        }
        List<Usuario> usuarios;
        if (conexao.getDev()) {
            usuarios = con.query("SELECT * FROM usuario WHERE email = ? AND senha = ?",
                    new BeanPropertyRowMapper<>(Usuario.class), email, senha);
        } else {
            usuarios = con.query("SELECT * FROM usuario WHERE email = ? AND senha = ?",
                    new BeanPropertyRowMapper<>(Usuario.class), email, senha);
        }
        if (usuarios.size() > 0) {
            String motivo = ":--SUCCESS: O Sistema localizou " + usuarios.size() + " registro(s)  relacionado ao email ('" + email + "') e a senha ('" + senha + "')!";
            logs.adicionarMotivo(motivo);

            if (usuarios.get(0).getFkTipoUsuario().equals(1)) {
                usuario = new AdmNowl(usuarios.get(0));
                System.out.println("Bem vindo ADM Nowl" + usuario.getNome());
            } else if (usuarios.get(0).getFkTipoUsuario().equals(2)) {
                usuario = new Adiministrador(usuarios.get(0));
                System.out.println("Bem vindo ADM " + usuario.getNome());
            } else if (usuarios.get(0).getFkTipoUsuario().equals(3)) {
                usuario = new Professor(usuarios.get(0));
                System.out.println("Bem vindo Professor " + usuario.getNome());
            } else {
                usuario = new Aluno(usuarios.get(0));
                System.out.println("Bem vindo Usuario" + usuario.getNome());
            }
            Integer opcaoUsuario;

            do {
                System.out.println("-".repeat(15));

//                if (usuario instanceof AdmNowl) {
//                    System.out.println("Opções de ADM Nowl");
//                    System.out.println("N1 - listar máquinas disponíveis de alguma empresa");
//                    System.out.println("N2 - listar usuários de alguma empresa");
//                    System.out.println("N3 - Cadastrar usuário ");
//                }

//                if (usuario instanceof Adiministrador) {
//                    System.out.println("Opções de Administrador");
//                    System.out.println("A1 - listar máquinas disponíveis da sua instituição");
//                    System.out.println("A2 - listar máquinas em uso da sua instituição");
//                    System.out.println("A3 - listar usuários da sua instituição");
//                    System.out.println("A4 - Cadastrar usuário ");
//                }
//                if (usuario instanceof Professor) {
//                    System.out.println("Opções de Professor");
//                    System.out.println("P1 - listar máquinas disponíveis da sua instituição");
//                    System.out.println("P2 - listar máquinas em uso da sua instituição");
//                    System.out.println("AP3 - listar usuários da sua instituição");
//                }

                System.out.println("----------|| Opções do sistema ||----------");
                System.out.println("1 - Ativar máquina");
                System.out.println("2 - Fechar sistema");
                System.out.println("-".repeat(15));

                opcaoUsuario = in.nextInt();

                switch (opcaoUsuario) {

                    case 1:
                        List<Maquina> maquinas;
                        if (conexao.getDev()) {
                            maquinas = con.query("SELECT * FROM maquina WHERE emUso = 0 AND fkInstituicao = ?",
                                    new BeanPropertyRowMapper<>(Maquina.class), usuario.getFkInstituicao());
                        } else {
                            maquinas = con.query("SELECT * FROM maquina WHERE emUso = 0 AND fkInstituicao = ?",
                                    new BeanPropertyRowMapper<>(Maquina.class), usuario.getFkInstituicao());
                        }
                        if (maquinas.size() > 0) {
                            String motivoMaquina = ":--SUCCESS: O Sistema localizou " + maquinas.size() + " maquina(s) registrada(s)  relacionadas a intituição!";
                            logs.adicionarMotivo(motivoMaquina);

                            System.out.println("-".repeat(15));
                            System.out.println("Escolha uma máquina disponível");

                            for (Maquina maquina : maquinas) {
                                System.out.println("id: " + maquina.getIdMaquina());
                                System.out.println("nome: " + maquina.getNome());
                                System.out.println("Sistema Operacional: " + maquina.getSO());
                                if (maquina.getDetalhes() != null) {
                                    System.out.println("Detalhes: " + maquina.getDetalhes());
                                }
                            }

                            System.out.println("-".repeat(15));
                            System.out.println("Digite o número da máquina");
                            Integer numMaquina = in.nextInt();
                            ativarMaquina(con, numMaquina, histConsmRecurso);
                            numeroMaquina = numMaquina;
                            List<Permissao> permissaos;
                            if (conexao.getDev()) {
                                permissaos = con.query("SELECT * FROM permissao WHERE emUso = 1",
                                        new BeanPropertyRowMapper<>(Permissao.class));
                            } else {
                                permissaos = con.query("SELECT * FROM permissao WHERE emUso = 1",
                                        new BeanPropertyRowMapper<>(Permissao.class));
                            }
                            System.out.println("-".repeat(15));
                            for (Permissao permissao : permissaos) {
                                System.out.println("Codigo aula: " + permissao.getNome());
                            }
                            System.out.println("-".repeat(15));
                            System.out.println("Digite o código da aula");
                            String codigoAula = leitor.nextLine();
                            histConsmRecurso.mostrarHistorico(numeroMaquina, codigoAula);
                            break;
                        } else {
                            String motivoMaquina = ":--ERROR: O Sistema não localizou registro de maquinas na sua intituição";
                            logs.adicionarMotivo(motivoMaquina);
                        }
                    case 2:
                        desativarMaquina(con, numeroMaquina);
                        exibirMensagemDespedida();
                        histConsmRecurso.fecharSistema();// Isso encerrará o programa
                        return;
                    default:
                        System.out.println("Opção inválida");
                }
            } while (opcaoUsuario != 2);
        } else {
            System.out.println("Dados de login inválidos");

            String motivo = ":--ERROR: O Sistema não localizou nenhum dado relacionado ao email ('" + email + "') e a senha ('" + senha + "')!";
            logs.adicionarMotivo(motivo);
        }
    }

    private static void ativarMaquina(JdbcTemplate con, Integer maquinaId, HistConsmRecurso histConsmRecurso) {
        Maquina maquina = con.queryForObject("SELECT * FROM maquina WHERE idMaquina = ? AND emUso = 0",
                new BeanPropertyRowMapper<>(Maquina.class), maquinaId);

        if (maquina != null) {
            if (conexao.getDev()) {

                con.update("UPDATE maquina SET emUso = 1 WHERE idMaquina = ?", maquinaId);
                System.out.println("Máquina ativada com sucesso: " + maquina.getNome());
            } else {
                con.update("UPDATE maquina SET emUso = 1 WHERE idMaquina = ?", maquinaId);
                System.out.println("Máquina ativada com sucesso: " + maquina.getNome());
            }
        } else {
            System.out.println("Máquina não disponível ou inválida.");
        }
    }

    private static void desativarMaquina(JdbcTemplate con, Integer maquinaId) {
        if (conexao.getDev()) {
            con.update("UPDATE maquina SET emUso = 0 WHERE idMaquina = ?", maquinaId);
        } else {
            con.update("UPDATE maquina SET emUso = 0 WHERE idMaquina = ?", maquinaId);

        }
    }

    private static void exibirMensagemDespedida() {
        System.out.println("+---------------------------------------------------------------+");
        System.out.println("|                            MAGISTER                           |");
        System.out.println("|             Obrigado por usar o nosso sistema                 |");
        System.out.println("|          Tenha um ótimo dia e uma vida incrível               |");
        System.out.println("|                                                               |");
        System.out.println("|                        #6D499D                                |");
        System.out.println("+---------------------------------------------------------------+");
    }

    private static void cadastrarMaquina(JdbcTemplate con) {
        Scanner leitor = new Scanner(System.in);

        System.out.println("Digite o nome da máquina:");
        String nomeMaquina = leitor.nextLine();

        System.out.println("Digite o sistema operacional da máquina:");
        String sistemaOperacional = leitor.nextLine();

        System.out.println("A máquina está em uso? (Digite 1 para sim, 0 para não):");
        int emUso = leitor.nextInt();

        System.out.println("Digite o ID da instituição (fkInstituicao):");
        int fkInstituicao = leitor.nextInt();

        // Cadastrar a máquina no banco de dados
        if (conexao.getDev()) {

            con.update("INSERT INTO maquina (nome, SO, emUso, fkInstituicao) VALUES (?, ?, ?, ?)", nomeMaquina, sistemaOperacional, emUso, fkInstituicao);
        } else {
            con.update("INSERT INTO maquina (nome, SO, emUso, fkInstituicao) VALUES (?, ?, ?, ?)", nomeMaquina, sistemaOperacional, emUso, fkInstituicao);

        }

        Integer idMaquina;
        if (conexao.getDev()) {
            idMaquina = con.queryForObject("SELECT MAX(idMaquina) FROM maquina", Integer.class);

        } else {
            idMaquina = con.queryForObject("SELECT MAX(idMaquina) FROM maquina", Integer.class);

        }
        // Recuperar o ID da máquina recém-cadastrada

        // Cadastrar hardware e componente para a máquina
        cadastrarHardwareEComponente(con, idMaquina);
        cadastrarHardwareEComponente(con, idMaquina);
        cadastrarHardwareEComponente(con, idMaquina);

        System.out.println("Máquina cadastrada com sucesso!");
    }

    private static Integer cadastrarTipoHardware(JdbcTemplate con) {
        Scanner leitor = new Scanner(System.in);

        System.out.println("Digite o nome do tipo de hardware (ex: CPU, GPU, Memória):");
        String tipoHardware = leitor.nextLine();

        // Cadastrar o tipo de hardware no banco de dados
        if (conexao.getDev()) {

            con.update("INSERT INTO tipoHardware (tipo) VALUES (?)", tipoHardware);
        } else {
            con.update("INSERT INTO tipoHardware (tipo) VALUES (?)", tipoHardware);

        }
        // Recuperar o ID do tipo de hardware recém-cadastrado
        if (conexao.getDev()) {

            return con.queryForObject("SELECT MAX(idTipoHardware) FROM tipoHardware", Integer.class);
        } else {
            return con.queryForObject("SELECT MAX(idTipoHardware) FROM tipoHardware", Integer.class);
        }
    }

    private static void cadastrarHardwareEComponente(JdbcTemplate con, Integer idMaquina) {
        Scanner leitor = new Scanner(System.in);

        // Cadastrar hardware
        System.out.println("Digite o fabricante do hardware:");
        String fabricante = leitor.nextLine();

        System.out.println("Digite o modelo do hardware:");
        String modelo = leitor.nextLine();

        System.out.println("Digite a capacidade do hardware:");
        double capacidade = leitor.nextDouble();


        System.out.println("Digite a especificidade do hardware:");
        String especificidade = leitor.nextLine();

        // Cadastrar o tipo de hardware
        System.out.println("Digite o tipo de hardware (ex: CPU, GPU, Memória):");
        String tipoHardware = leitor.nextLine();

        // Cadastrar o tipo de hardware no banco de dados
        if (conexao.getDev()) {
            con.update("INSERT INTO tipoHardware (tipo) VALUES (?)", tipoHardware);
        } else {
            con.update("INSERT INTO tipoHardware (tipo) VALUES (?)", tipoHardware);

        }
        // Recuperar o ID do tipo de hardware recém-cadastrado
        Integer fkTipoHardware;
        if (conexao.getDev()) {
            fkTipoHardware = con.queryForObject("SELECT MAX(idTipoHardware) FROM tipoHardware", Integer.class);

        } else {
            fkTipoHardware = con.queryForObject("SELECT MAX(idTipoHardware) FROM tipoHardware", Integer.class);

        }

        // Cadastrar o hardware no banco de dados, incluindo fkTipoHardware
        if (conexao.getDev()) {

            con.update("INSERT INTO hardware (fabricante, modelo, capacidade, especificidade, fkTipoHardware) VALUES (?, ?, ?, ?, ?)", fabricante, modelo, capacidade, especificidade, fkTipoHardware);
        } else {
            con.update("INSERT INTO hardware (fabricante, modelo, capacidade, especificidade, fkTipoHardware) VALUES (?, ?, ?, ?, ?)", fabricante, modelo, capacidade, especificidade, fkTipoHardware);
        }
        // Recuperar o ID do hardware recém-cadastrado
        Integer idHardware;
        if (conexao.getDev()) {
            idHardware = con.queryForObject("SELECT MAX(idHardware) FROM hardware", Integer.class);
        } else {
            idHardware = con.queryForObject("SELECT MAX(idHardware) FROM hardware", Integer.class);
        }
        // Cadastrar componente
        System.out.println("Digite a porcentagem máxima para o componente (deixe em branco para usar o valor padrão):");
        String inputMax = leitor.nextLine();
        Integer max = (inputMax.isEmpty()) ? null : Integer.parseInt(inputMax);

        // Cadastrar o componente no banco de dados, incluindo fkTipoHardware
        if (conexao.getDev()) {

            con.update("INSERT INTO componente (max, fkMaquina, fkHardware) VALUES (?, ?, ?)", max, idMaquina, idHardware);
        }else{
            con.update("INSERT INTO componente (max, fkMaquina, fkHardware) VALUES (?, ?, ?)", max, idMaquina, idHardware);

        }

        System.out.println("Hardware e componente cadastrados com sucesso!");
    }
}