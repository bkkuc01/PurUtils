package pl.bkkuc.purutils.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataBase {

    Connection getConnection() throws SQLException;

    void initDataBase(@NotNull String... queries) throws SQLException;

    void close();

    DataBaseType getDataBaseType();

    @Nullable String getDataBaseName();
}
