package sistemaCaptura;

import sistemaCaptura.conexao.Conexao;
import sistemaCaptura.user.Usuario;
import sistemaCaptura.user.Adiministrador;
import sistemaCaptura.user.Professor;
import sistemaCaptura.user.AdmNowl;
import sistemaCaptura.user.Aluno;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Scanner;

public class AppHistorico {

    public static void main(String[] args) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        HistConsmRecurso histConsmRecurso = new HistConsmRecurso();

        Scanner in = new Scanner(System.in);
        Integer escolha;

        do {
            System.out.println("-".repeat(15));
            System.out.println("Bem vindo ao sistema Nowl");
            System.out.println("Escolha uma das opções abaixo");
            System.out.println("1 - Fazer login");
            System.out.println("2 - Sair");
            System.out.println("-".repeat(15));

            escolha = in.nextInt();

            switch (escolha) {
                case 1:
//                    cadastrarUsuario(con);
//                    break;
                    fazerLogin(con, histConsmRecurso, in);
                    break;
                case 2:
                    exibirMensagemDespedida();
                    histConsmRecurso.fecharSistema();
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
        con.update("INSERT INTO usuario (nome, email, senha, fkInstituicao, fkTipoUsuario) VALUES (?, ?, ?, ?, ?)", nome, email, senha, fkInstituicao, fkTipoUsuario);

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

        List<Usuario> usuarios = con.query("SELECT * FROM usuario WHERE email = ? AND senha = ?",
                new BeanPropertyRowMapper<>(Usuario.class), email, senha);

        if (usuarios.size() > 0) {
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

                if (usuario instanceof AdmNowl) {
                    System.out.println("Opções de ADM Nowl");
                    System.out.println("N1 - listar máquinas disponiveis de alguma empresa");
                    System.out.println("N2 - listar usuarios de alguma empresa");
                    System.out.println("N3 - Cadastrar usuario ");
                }

                if (usuario instanceof Adiministrador) {
                    System.out.println("Opções de Adiministrador");
                    System.out.println("A1 - listar máquinas disponiveis da sua instituição");
                    System.out.println("A2 - listar máquinas em uso da sua instituição");
                    System.out.println("A3 - listar usuarios da sua intituição");
                    System.out.println("A4 - Cadastrar usuario ");

                }
                if (usuario instanceof Professor) {
                    System.out.println("Opções de Professor");
                    System.out.println("P1 - listar máquinas disponiveis da sua instituição");
                    System.out.println("P2 - listar máquinas em uso da sua instituição");
                    System.out.println("AP3 - listar usuarios da sua intituição");

                }

                System.out.println("----------|| Opções do sistema ||----------");
                System.out.println("1 - Ativar máquina");
                System.out.println("2 - Fechar sistema");
                System.out.println("-".repeat(15));

                opcaoUsuario = in.nextInt();

                switch (opcaoUsuario) {
                    case 1:
                        List<Maquina> maquinas = con.query("SELECT * FROM maquina WHERE emUso = 0",
                                new BeanPropertyRowMapper<>(Maquina.class));

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
                        System.out.println("Digite o codigo da aula");
                        String codigoAula = leitor.nextLine();
                        histConsmRecurso.mostrarHistorico(numeroMaquina,codigoAula);
                        break;

                    case 2:
                        desativarMaquina(con, numeroMaquina);
                        exibirMensagemDespedida();
                        histConsmRecurso.fecharSistema();
                        return; // Isso encerrará o programa
                    default:
                        System.out.println("Opção inválida");
                }
            } while (opcaoUsuario != 2);
        } else {
            System.out.println("Dados de login inválidos");
        }
    }


    private static void ativarMaquina(JdbcTemplate con, Integer maquinaId, HistConsmRecurso histConsmRecurso) {
        Maquina maquina = con.queryForObject("SELECT * FROM maquina WHERE idMaquina = ? AND emUso = 0",
                new BeanPropertyRowMapper<>(Maquina.class), maquinaId);

        if (maquina != null) {

            con.update("UPDATE maquina SET emUso = 1 WHERE idMaquina = ?", maquinaId);
            System.out.println("Máquina ativada com sucesso: " + maquina.getNome());

            // Agora, você tem as informações corretas em maquina.getSistemaOperacional() e maquina.getRamTotal()
        } else {
            System.out.println("Máquina não disponível ou inválida.");
        }
    }

    private static void desativarMaquina(JdbcTemplate con, Integer maquinaId) {
        con.update("UPDATE maquina SET emUso = 0 WHERE idMaquina = ?", maquinaId);
    }

    private static void exibirMensagemDespedida() {
        System.out.println("+---------------------------------------------------------------+");
        System.out.println("|                            \u001B[38;2;109;73;157mMAGISTER\u001B[0m                           |");
        System.out.println("|             \u001B[38;2;109;73;157mObrigado por usar o nosso sistema\u001B[0m                 |"); // Cor personalizada
        System.out.println("|          \u001B[38;2;109;73;157mTenha um ótimo dia e uma vida incrível\u001B[0m               |"); // Cor personalizada
        System.out.println("|                                                               |");
        System.out.println("|                        \u001B[38;2;109;73;157m#6D499D\u001B[0m                                |"); // Cor personalizada
        System.out.println("+---------------------------------------------------------------+");
    }

}