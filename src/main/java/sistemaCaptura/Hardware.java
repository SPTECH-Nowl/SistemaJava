package sistemaCaptura;

public class Hardware {

    private Integer idHardware;
    private String fabricante;
    private String modelo;
    private Integer capacidade;
    private String especificidade;
    private Integer fkTipoHardware;

    public Hardware(){}


    public Hardware(Integer idHardware, String fabricante, String modelo, Integer capacidade, String especificidade, Integer fkTipoHardware) {
        this.idHardware = idHardware;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.capacidade = capacidade;
        this.especificidade = especificidade;
        this.fkTipoHardware = fkTipoHardware;
    }

    public Integer getIdHardware() {
        return idHardware;
    }

    public void setIdHardware(Integer idHardware) {
        this.idHardware = idHardware;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public String getEspecificidade() {
        return especificidade;
    }

    public void setEspecificidade(String especificidade) {
        this.especificidade = especificidade;
    }

    public Integer getFkTipoHardware() {
        return fkTipoHardware;
    }

    public void setFkTipoHardware(Integer fkTipoHardware) {
        this.fkTipoHardware = fkTipoHardware;
    }

    @Override
    public String toString() {
        return "Hardware{" +
                "idHardware=" + idHardware +
                ", fabricante='" + fabricante + '\'' +
                ", modelo='" + modelo + '\'' +
                ", capacidade=" + capacidade +
                ", especificidade='" + especificidade + '\'' +
                ", fkTipoHardware=" + fkTipoHardware +
                '}';
    }
}
