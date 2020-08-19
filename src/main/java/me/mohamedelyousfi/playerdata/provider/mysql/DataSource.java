package me.mohamedelyousfi.playerdata.provider.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.mohamedelyousfi.playerdata.Playerdata;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource dataSource;

    static {
        Playerdata plugin = Playerdata.getInstance();

        // De database credentials uit de config file halen
        String host = plugin.getConfig().getString("mysql.host");
        String username = plugin.getConfig().getString("mysql.username");
        String password = plugin.getConfig().getString("mysql.password");
        String database = plugin.getConfig().getString("mysql.database");

        // HikariCP initialisen en connecten met de database.
        config.setJdbcUrl("jdbc:mysql://" + host + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    private DataSource() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
