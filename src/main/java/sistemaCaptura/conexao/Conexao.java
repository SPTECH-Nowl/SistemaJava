package sistemaCaptura.conexao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;

public class Conexao {

    private Boolean dev = false;// true = desenvolvimento(MYSQL) |--||--| false = produção(SQL)
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



            dataSource.setUrl("jdbc:sqlserver://52.3.127.92;" +
                    "database=magister;" +
                    "user=sa;" +
                    "password=Magister123@;" +
                    "trustServerCertificate=true;");
            dataSource.setUsername("sa");
            dataSource.setPassword("Magister123@");



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