package ru.lookzbs.sql;

import org.sql2o.Sql2o;
import ru.lookzbs.Core;

import javax.sql.DataSource;
import java.util.Properties;

public class SqlConnector {

    public static Sql2o sql2o;

    private static String host;
    private static String username;
    private static String password;

    public SqlConnector() {
        Properties props = Core.loadConfig();

        if (sql2o == null) {
            //loadDriver();

            host = props.getProperty("dbhost");
            username = props.getProperty("dbusername");
            password = props.getProperty("dbpassword");

            sql2o = new Sql2o(host, username, password);
        }
    }

    private void loadDriver() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connection succesfull!");
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }
}
