package pl.bkkuc.purutils.database.databases;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.purutils.database.Connectable;
import pl.bkkuc.purutils.database.DataBase;
import pl.bkkuc.purutils.database.DataBaseType;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreSQL implements DataBase, Connectable {

    private final HikariDataSource dataSource;

    @NotNull
    private final String host, port, username, password, databaseName;

    public PostgreSQL(@NotNull String host, @NotNull String port, @NotNull String username, @NotNull String password, @NotNull String databaseName) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + databaseName);
        config.setUsername(username);
        config.setPassword(password);

        config.setMaximumPoolSize(10);
        config.setPoolName(databaseName + "-pool");

        dataSource = new HikariDataSource(config);
    }

    @Override
    public void initDataBase(@NotNull String... queries) {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();

            for (String query : queries) {
                statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Could not initialize database schema!");
        }
    }

    @Override
    public void close() {
        dataSource.close();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public DataBaseType getDataBaseType() {
        return DataBaseType.POSTGRESQL;
    }

    @Override
    public @Nullable String getDataBaseName() {
        return databaseName;
    }

    @Override
    public @NotNull String getHost() {
        return host;
    }

    @Override
    public @NotNull String getPort() {
        return port;
    }

    @Override
    public @NotNull String getUserName() {
        return username;
    }

    @Override
    public @NotNull String getPassword() {
        return password;
    }
}
