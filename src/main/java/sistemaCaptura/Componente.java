package sistemaCaptura;

public class Componente {

    private Integer idComponente;
    private int max;
    private Integer fkMaquina;
    private Integer fkHardware;

public  Componente(){}
    public Componente(Integer idComponente, int max, Integer fkMaquina, Integer fkHardware) {
        this.idComponente = idComponente;
        this.max = max;
        this.fkMaquina = fkMaquina;
        this.fkHardware = fkHardware;
    }

    public Integer getIdComponente() {
        return idComponente;
    }

    public void setIdComponente(Integer idComponente) {
        this.idComponente = idComponente;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Integer getFkMaquina() {
        return fkMaquina;
    }

    public void setFkMaquina(Integer fkMaquina) {
        this.fkMaquina = fkMaquina;
    }

    public Integer getFkHardware() {
        return fkHardware;
    }

    public void setFkHardware(Integer fkHardware) {
        this.fkHardware = fkHardware;
    }

    @Override
    public String toString() {
        return "Componente{" +
                "idComponente=" + idComponente +
                ", max=" + max +
                ", fkMaquina=" + fkMaquina +
                ", fkHardware=" + fkHardware +
                '}';
    }
}
