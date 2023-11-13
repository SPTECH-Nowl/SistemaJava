package sistemaCaptura;

public class PermissaoProcesso {
    private Integer idPermissaoProcesso;
    private Integer fkProcesso;
    private Integer fkPermissao;
    private String dataAlocacao;
    public PermissaoProcesso(){}
    public PermissaoProcesso(Integer idPermissaoProcesso, Integer fkProcesso, Integer fkPermissao, String dataAlocacao) {
        this.idPermissaoProcesso = idPermissaoProcesso;
        this.fkProcesso = fkProcesso;
        this.fkPermissao = fkPermissao;
        this.dataAlocacao = dataAlocacao;
    }

    public Integer getIdPermissaoProcesso() {
        return idPermissaoProcesso;
    }

    public void setIdPermissaoProcesso(Integer idPermissaoProcesso) {
        this.idPermissaoProcesso = idPermissaoProcesso;
    }

    public Integer getFkProcesso() {
        return fkProcesso;
    }

    public void setFkProcesso(Integer fkProcesso) {
        this.fkProcesso = fkProcesso;
    }

    public Integer getFkPermissao() {
        return fkPermissao;
    }

    public void setFkPermissao(Integer fkPermissao) {
        this.fkPermissao = fkPermissao;
    }

    public String getDataAlocacao() {
        return dataAlocacao;
    }

    public void setDataAlocacao(String dataAlocacao) {
        this.dataAlocacao = dataAlocacao;
    }

    @Override
    public String toString() {
        return "PermissaoProcesso{" +
                "idPermissaoProcesso=" + idPermissaoProcesso +
                ", fkProcesso=" + fkProcesso +
                ", fkPermissao=" + fkPermissao +
                ", dataAlocacao='" + dataAlocacao + '\'' +
                '}';
    }
}
