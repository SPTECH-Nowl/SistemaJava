package sistemaCaptura.user;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import sistemaCaptura.conexao.Conexao;

import java.util.ArrayList;
import java.util.List;

public class AdmNowl extends Usuario {
    static Conexao conexao = new Conexao();
    List<Usuario> usuarios;

    public AdmNowl() {
        usuarios = new ArrayList<>();
    }

    public AdmNowl(Usuario usuario) {
        super(usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getFkInstituicao(), usuario.getFkTipoUsuario());
        usuarios = new ArrayList<>();
    }

    public AdmNowl(Integer idUsuario, String nome, String email, String senha, Integer fkInstituicao, Integer fkTipoUsuario) {
        super(idUsuario, nome, email, senha, fkInstituicao, fkTipoUsuario);
    }

    public void opcaoAdmNowl() {
        JdbcTemplate con;
        if (conexao.getDev()) {
            con = conexao.getConexaoDoBancoMySQL();
        } else {
            con = conexao.getConexaoDoBancoSQLServer();
        }

        usuarios = con.query("SELECT * FROM usuario",
                new BeanPropertyRowMapper<>(Usuario.class));
        System.out.println("Lista de Usuários do sistema");

        for (Usuario usuario : usuarios) {
            if (usuario.getFkTipoUsuario() == 1) {
                System.out.println("|Nome: " + usuario.getNome() + "| ADM Nowl|");
            } else if (usuario.getFkTipoUsuario() == 2) {
                System.out.println("|Nome: " + usuario.getNome() + "| ADM da Instituição|");
            } else if (usuario.getFkTipoUsuario() == 3) {
                System.out.println("|Nome: " + usuario.getNome() + "| Professor|");
            } else {
                System.out.println("|Nome: " + usuario.getNome() + "| Usuário padrão|");
            }
        }
    }

    public void listar() {
        System.out.println("Listar TUDO");
    }

    @Override
    public String toString() {
        return "AdmNowl{} " + super.toString();
    }
}
