package sistemaCaptura;

public class Permissao {
    private Integer idPermissao;
    private String nome;
    private Integer emUso;
     private Integer duracaoStrikePadrao;
    private Integer fkAtuacao;
    private Integer fkUsuario;

    public Permissao() {
    }

    public Permissao(Integer idPermissao, String nome, Integer emUso, Integer duracaoStrikePadrao, Integer fkAtuacao, Integer fkUsuario) {
        this.idPermissao = idPermissao;
        this.nome = nome;
        this.emUso = emUso;
        this.duracaoStrikePadrao = duracaoStrikePadrao;
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

    public Integer getEmUso() {
        return emUso;
    }

    public void setEmUso(Integer emUso) {
        this.emUso = emUso;
    }

    public Integer getDuracaoStrikePadrao() {
        return duracaoStrikePadrao;
    }

    public void setDuracaoStrikePadrao(Integer duracaoStrikePadrao) {
        this.duracaoStrikePadrao = duracaoStrikePadrao;
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
                ", emUso=" + emUso +
                ", duracaoStrikePadrao=" + duracaoStrikePadrao +
                ", fkAtuacao=" + fkAtuacao +
                ", fkUsuario=" + fkUsuario +
                '}';
    }
}
