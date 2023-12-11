package sistemaCaptura.user;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import sistemaCaptura.conexao.Conexao;

import java.util.ArrayList;
import java.util.List;

public class Adiministrador extends Usuario {

    static Conexao conexao = new Conexao();
    List<Usuario> usuarios;

    public Adiministrador(){
        usuarios=  new ArrayList<>();
    }

    public Adiministrador(Usuario usuario) {
        super(usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getFkInstituicao(), usuario.getFkTipoUsuario());
        usuarios=  new ArrayList<>();
    }

    public Adiministrador(Integer idUsuario, String nome, String email, String senha, Integer fkInstituicao, Integer fkTipoUsuario) {
        super(idUsuario, nome, email, senha, fkInstituicao, fkTipoUsuario);
    }

    public void addUsuario(){}

    public void deletarUsuario(){}

    public void listar() {
        System.out.println("Listar usuários e máquinas da sua empresa");
    }

    public void opcaoAdiministrador() {
        JdbcTemplate con;
        if (conexao.getDev()) {
            con = conexao.getConexaoDoBancoMySQL();
        } else {
            con = conexao.getConexaoDoBancoSQLServer();
        }

        usuarios = con.query("SELECT * FROM usuario WHERE fkInstituicao = ?",
                new BeanPropertyRowMapper<>(Usuario.class), getFkInstituicao());
        System.out.println("Lista de Usuários da sua empresa");

        for (Usuario usuario : usuarios){
            System.out.println("Nome: "+ usuario.getNome());
        }
    }

    @Override
    public String toString() {
        return "Adiministrador{} " + super.toString();
    }
}
