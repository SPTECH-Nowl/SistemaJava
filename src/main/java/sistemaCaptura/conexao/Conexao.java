package sistemaCaptura.conexao;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {

    private Boolean dev = false; // true = desenvolvimento(MYSQL) |--||--| false = produção(SQL)
    private JdbcTemplate conexaoDoBancoMySQL;
    private JdbcTemplate conexaoDoBancoSQLServer;

    public Conexao() {
        if (dev) {
            conexaoDoBancoMySQL = configurarConexaoMySQL();
        } else {
            conexaoDoBancoSQLServer = configurarConexaoSQLServer();
        }
    }

    private JdbcTemplate configurarConexaoMySQL() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/magister");
        dataSource.setUsername("aluno");
        dataSource.setPassword("aluno");
        return new JdbcTemplate(dataSource);
    }

    private JdbcTemplate configurarConexaoSQLServer() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://52.3.127.92;database=magister;user=sa;password=Magister123@;trustServerCertificate=true;");
        return new JdbcTemplate(dataSource);
    }

    public Boolean getDev() {
        return dev;
    }

    public void setDev(Boolean dev) {
        this.dev = dev;
    }

    public JdbcTemplate getConexaoDoBancoMySQL() {
        return conexaoDoBancoMySQL;
    }

    public JdbcTemplate getConexaoDoBancoSQLServer() {
        return conexaoDoBancoSQLServer;
    }
}
