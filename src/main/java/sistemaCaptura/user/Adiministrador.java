package sistemaCaptura.user;

public class Adiministrador extends Usuario {

    public Adiministrador(){}
    public Adiministrador(Usuario usuario) {
        super(usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getFkInstituicao(), usuario.getFkTipoUsuario());
    }

    public Adiministrador(Integer idUsuario, String nome, String email, String senha, Integer fkInstituicao, Integer fkTipoUsuario) {
        super(idUsuario, nome, email, senha, fkInstituicao, fkTipoUsuario);
    }

    public void addUsuario(){}

    public void deletarUsuario(){}

    public void listar() {
        System.out.println("lisar usuarios e maquinas da sua empresa");
    }

    @Override
    public String toString() {
        return "Adiministrador{} " + super.toString();
    }
}
