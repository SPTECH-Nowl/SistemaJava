package sistemaCaptura.user;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import sistemaCaptura.conexao.Conexao;

import java.util.List;

public class Aluno extends Usuario {
    static Conexao conexao = new Conexao();

    public Aluno() {}

    public Aluno(Usuario usuario) {
        super(usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getFkInstituicao(), usuario.getFkTipoUsuario());
    }

    public Aluno(Integer idUsuario, String nome, String email, String senha, Integer fkInstituicao, Integer fkTipoUsuario) {
        super(idUsuario, nome, email, senha, fkInstituicao, fkTipoUsuario);
    }

    public void opcaoAluno() {
        System.out.println("Essa opção está bloqueada para o seu nível de usuário");
    }

    @Override
    public String toString() {
        return "Aluno{} " + super.toString();
    }

    public void listarAlunos() {
        JdbcTemplate con;
        if (conexao.getDev()) {
            con = conexao.getConexaoDoBancoMySQL();
        } else {
            con = conexao.getConexaoDoBancoSQLServer();
        }

        List<Usuario> alunos = con.query("SELECT * FROM usuario WHERE fkInstituicao = ? AND fkTipoUsuario = ?",
                new BeanPropertyRowMapper<>(Usuario.class), getFkInstituicao(), 1);

        System.out.println("Lista de Alunos:");
        for (Usuario aluno : alunos) {
            System.out.println("Nome: " + aluno.getNome());
        }
    }
}
