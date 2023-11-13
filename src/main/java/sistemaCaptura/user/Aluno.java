package sistemaCaptura.user;

public class Aluno extends Usuario{

    public  Aluno(){}

    public Aluno(Usuario usuario) {
        super(usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getFkInstituicao(), usuario.getFkTipoUsuario());
    }

    public Aluno(Integer idUsuario, String nome, String email, String senha, Integer fkInstituicao, Integer fkTipoUsuario) {
        super(idUsuario, nome, email, senha, fkInstituicao, fkTipoUsuario);
    }

    public void listar() {
        System.out.println("Não tem autorização para executar esse comando");
    }

    @Override
    public String toString() {
        return "AdmNowl{} " + super.toString();
    }

}
