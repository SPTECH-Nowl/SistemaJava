package sistemaCaptura.user;

public class AdmNowl extends Usuario{
    public AdmNowl(){}
    public AdmNowl(Usuario usuario) {
        super(usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getFkInstituicao(), usuario.getFkTipoUsuario());
    }
    public AdmNowl(Integer idUsuario, String nome, String email, String senha, Integer fkInstituicao, Integer fkTipoUsuario) {
        super(idUsuario, nome, email, senha, fkInstituicao, fkTipoUsuario);
    }

    public void listar() {
        System.out.println("Listar TUDO");
    }

    @Override
    public String toString() {
        return "AdmNowl{} " + super.toString();
    }
}
