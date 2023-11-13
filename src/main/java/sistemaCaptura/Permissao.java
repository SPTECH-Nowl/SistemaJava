package sistemaCaptura;

public class Permissao {
    private Integer idPermissao;
    private String nome;
    private Integer fkAtuacao;
    private Integer fkUsuario;

    public Permissao() {
    }

    public Permissao(Integer idPermissao, String nome, Integer fkAtuacao, Integer fkUsuario) {
        this.idPermissao = idPermissao;
        this.nome = nome;
        this.fkAtuacao = fkAtuacao;
        this.fkUsuario = fkUsuario;
    }

    public Integer getIdPermissao() {
        return idPermissao;
    }

    public void setIdPermissao(Integer idPermissao) {
        this.idPermissao = idPermissao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getFkAtuacao() {
        return fkAtuacao;
    }

    public void setFkAtuacao(Integer fkAtuacao) {
        this.fkAtuacao = fkAtuacao;
    }

    public Integer getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(Integer fkUsuario) {
        this.fkUsuario = fkUsuario;
    }

    @Override
    public String toString() {
        return "Permissao{" +
                "idPermissao=" + idPermissao +
                ", nome='" + nome + '\'' +
                ", fkAtuacao=" + fkAtuacao +
                ", fkUsuario=" + fkUsuario +
                '}';
    }
}
