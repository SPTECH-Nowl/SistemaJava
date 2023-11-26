package sistemaCaptura.user;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import sistemaCaptura.conexao.Conexao;

import java.util.List;

public class Aluno extends Usuario{
    static Conexao conexao = new Conexao();
    public  Aluno(){}

    public Aluno(Usuario usuario) {
        super(usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getFkInstituicao(), usuario.getFkTipoUsuario());
    }

    public Aluno(Integer idUsuario, String nome, String email, String senha, Integer fkInstituicao, Integer fkTipoUsuario) {
        super(idUsuario, nome, email, senha, fkInstituicao, fkTipoUsuario);
    }
    public void opcaoAluno() {
        JdbcTemplate con = conexao.getConexaoDoBanco();

        System.out.println("Essa opção esta bloqueada para o seu nivel de usuario");
    }
    public String toString() {
        return "aluno{} " + super.toString();
    }

}
