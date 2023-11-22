package sistemaCaptura.conexao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;

public class Conexao {

    private Boolean dev = true;// true = desenvolvimento(MYSQL) |--||--| false = produção(SQL)
    private JdbcTemplate conexaoDoBanco;

    public Conexao() {

        if (dev) {
            BasicDataSource dataSource = new BasicDataSource();
        /*
             Exemplo de driverClassName:
                com.mysql.cj.jdbc.Driver <- EXEMPLO PARA MYSQL
                com.microsoft.sqlserver.jdbc.SQLServerDriver <- EXEMPLO PARA SQL SERVER
        */
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        /*
             Exemplo de string de conexões:
                jdbc:mysql://localhost:3306/mydb <- EXEMPLO PARA MYSQL
                jdbc:sqlserver://localhost:1433;database=mydb <- EXEMPLO PARA SQL SERVER
        */
            dataSource.setUrl("jdbc:mysql://localhost:3306/magister");
            dataSource.setUsername("aluno");
            dataSource.setPassword("aluno");

            conexaoDoBanco = new JdbcTemplate(dataSource);
        } else {
            BasicDataSource dataSource = new BasicDataSource();

            dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            dataSource.setUrl("jdbc:sqlserver://servidor-magister.database.windows.net:1433;database=bd-magister;user=admin-magister@servidor-magister;password=#Gfgrupo5;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

            dataSource.setUsername("admin-magister");

            dataSource.setPassword("#Gfgrupo5");


            this.conexaoDoBanco = new JdbcTemplate(dataSource);

        }

    }

    public Boolean getDev() {
        return dev;
    }

    public void setDev(Boolean dev) {
        this.dev = dev;
    }

    public JdbcTemplate getConexaoDoBanco() {
        return conexaoDoBanco;
    }
}
