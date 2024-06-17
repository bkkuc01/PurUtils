package pl.bkkuc.purutils.database;

public enum DataBaseType {
    MYSQL("MySQL"),
    SQLITE("SqLite"),
    MARIADB("MariaBD"),
    POSTGRESQL("Postgresql");

    private final String name;

    DataBaseType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
