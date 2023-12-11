package sistemaCaptura.user;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import sistemaCaptura.conexao.Conexao;

import java.util.ArrayList;
import java.util.List;

public class Professor extends Usuario {
    static Conexao conexao = new Conexao();
    List<Usuario> usuarios;

    public Professor() {
        usuarios = new ArrayList<>();
    }

    public Professor(Usuario usuario) {
        super(usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getFkInstituicao(), usuario.getFkTipoUsuario());
        usuarios = new ArrayList<>();
    }

    public Professor(Integer idUsuario, String nome, String email, String senha, Integer fkInstituicao, Integer fkTipoUsuario) {
        super(idUsuario, nome, email, senha, fkInstituicao, fkTipoUsuario);
    }

    public void opcaoProfessor() {
        JdbcTemplate con;
        if (conexao.getDev()) {
            con = conexao.getConexaoDoBancoMySQL();
        } else {
            con = conexao.getConexaoDoBancoSQLServer();
        }

        usuarios = con.query("SELECT * FROM usuario WHERE fkInstituicao = ? AND fkTipoUsuario=?",
                new BeanPropertyRowMapper<>(Usuario.class), getFkInstituicao(), 3);
        System.out.println("Lista de Professores");

        for (Usuario usuario : usuarios) {
            System.out.println("Nome: " + usuario.getNome());
        }
    }

    public void listar() {
        System.out.println("Listar Alunos e maquinas");
    }

    @Override
    public String toString() {
        return "Professor{} " + super.toString();
    }
}
