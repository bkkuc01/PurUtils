package pl.bkkuc.purutils.database;

import org.jetbrains.annotations.NotNull;

public interface Connectable {

    @NotNull String getHost();
    @NotNull String getPort();
    @NotNull String getUserName();
    @NotNull String getPassword();

}
