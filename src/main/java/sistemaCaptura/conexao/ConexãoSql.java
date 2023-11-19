package sistemaCaptura.conexao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
public class ConexãoSql {
    private JdbcTemplate connection;

    public ConexãoSql() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        dataSource.setUrl("jdbc:sqlserver://servidor-magister.database.windows.net:1433;database=bd-magister;user=admin-magister@servidor-magister;password=#Gfgrupo5;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

        dataSource.setUsername("admin-magister");

        dataSource.setPassword("#Gfgrupo5");


        this.connection = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getConnection() {
        return connection;
    }
}