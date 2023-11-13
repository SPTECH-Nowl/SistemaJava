package sistemaCaptura.user;

public class Professor extends Usuario {

    public  Professor(){}
    public Professor(Usuario usuario) {
        super(usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getFkInstituicao(), usuario.getFkTipoUsuario());
    }

    public Professor(Integer idUsuario, String nome, String email, String senha, Integer fkInstituicao, Integer fkTipoUsuario) {
        super(idUsuario, nome, email, senha, fkInstituicao, fkTipoUsuario);
    }


    public void listar() {
        System.out.println("Listar Alunos e maquinas");
    }

    @Override
    public String toString() {
        return "Professor{} " + super.toString();
    }
}
