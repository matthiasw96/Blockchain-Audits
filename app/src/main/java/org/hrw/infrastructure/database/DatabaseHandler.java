package org.hrw.infrastructure.database;

import org.hrw.datamodels.Datastructure;
import org.hrw.mapping.Converter;
import org.hrw.datamodels.ServerRecord;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DatabaseHandler {
    private final String hostAddress;
    private final String username;
    private final String password;
    private final Converter converter;
    private final int port;
    private final DateTimeFormatter FORMATTER;

    public DatabaseHandler(DatabaseHandlerBuilder builder) {
        this.hostAddress = builder.hostAddress;
        this.username = builder.username;
        this.password = builder.password;
        this.FORMATTER = builder.FORMATTER;
        this.port = builder.port;
        this.converter = builder.converter;
    }

    /**
     * Reads server data from the given table between the specified
     * timestamp range.
     *
     * <p>Timestamps are expected to be epoch seconds. The result set is converted
     * into {@link ServerRecord} objects via the configured {@link Converter}.</p>
     *
     * @param startPoint lower bound (inclusive) as epoch seconds
     * @param endPoint   upper bound (inclusive) as epoch seconds
     * @param tableName  database table name to query (e.g. {@code "serverdata"})
     * @return list of matching {@link ServerRecord} entries
     * @throws SQLException if the query or connection fails
     */
    public List<ServerRecord> readFromDatabase(long startPoint, long endPoint, String tableName) throws SQLException {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Reading from database");

        Statement statement = createStatement();
        String query = createSelectQuery(startPoint, endPoint, tableName);
        ResultSet resultSet = statement.executeQuery(query);
        List<ServerRecord> records = converter.resultSetToServerRecord(resultSet);

        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Data received");
        return records;
    }

    /**
     * Writes a list of data records to the specified table.
     *
     * <p>The {@link Datastructure} elements are converted to column lists and
     * value tuples.</p>
     *
     * <p>Conflicts on the {@code timestamp} column are ignored
     * using {@code ON CONFLICT (timestamp) DO NOTHING}.</p>
     *
     * @param data      list of records to write
     * @param tableName target table name
     * @throws SQLException if the insert or connection fails
     */
    public void writeToDatabase(List<? extends Datastructure> data, String tableName) throws SQLException {
        try {
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Connecting to database");

            Statement statement = createStatement();
            String query = createInsertQuery(data, tableName);
            statement.executeUpdate(query);

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Data stored");
        } catch (Exception e) {
            System.out.println(LocalDateTime.now().format(FORMATTER) + "Connecting to database failed");
            throw e;
        }
    }

    /**
     * Creates a new JDBC {@link Statement} using the configured PostgreSQL connection parameters.
     *
     * @return an open {@link Statement} for executing SQL queries
     * @throws SQLException if the connection cannot be established
     */
    private Statement createStatement() throws SQLException {
        String url = "jdbc:postgresql://" + hostAddress + ":"+port+"/postgres";
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection.createStatement();
    }

    /**
     * Builds an {@code INSERT} SQL statement for the given data list and table.
     *
     * <p>Conflicts on the {@code timestamp} column are ignored
     * using {@code ON CONFLICT (timestamp) DO NOTHING} to prevent overwriting of entries.</p>
     *
     * @param data      list of data records
     * @param tableName target table name
     * @return SQL insert statement string
     */
    private String createInsertQuery(List<? extends Datastructure> data, String tableName) {
        String columnNames = data.getFirst().getAttributeNames();
        String values = "";

        for(int i = 0; i < data.size(); i++) {
            values += "(" + data.get(i).toString() + ")";
            values += (i == data.size() - 1) ? "\n" : ",\n";
        }

        return """
                INSERT INTO\s""" + tableName + """
                 (
                """ + columnNames + """
                \n) \nVALUES
                """ + values + """
                ON CONFLICT (timestamp) DO NOTHING;""";
    }

    /**
     * Builds a {@code SELECT} SQL statement for fetching records between two timestamps.
     *
     * @param startPoint lower bound (inclusive) as epoch seconds
     * @param endPoint   upper bound (inclusive) as epoch seconds
     * @param tableName  table to query
     * @return SQL select statement string
     */
    private String createSelectQuery(long startPoint, long endPoint, String tableName){
        return """
                SELECT * FROM\s""" + tableName + """
                \sWHERE "timestamp" >= """ + startPoint + """
                \sAND "timestamp" <= """ + endPoint + """
                """;
    }

    /**
     * Builder for {@link DatabaseHandler} instances.
     *
     * <p>Allows configuration of database connection details and the
     * {@link Converter} used to map SQL results to domain models.</p>
     */
    public static class DatabaseHandlerBuilder {
        String hostAddress;
        String username;
        String password;
        int port;
        Converter converter;
        DateTimeFormatter FORMATTER;

        public DatabaseHandlerBuilder setHostAddress(String hostAddress) {
            this.hostAddress = hostAddress;
            return this;
        }

        public DatabaseHandlerBuilder setPort(int port) {
            this.port = port;
            return this;
        }

        public DatabaseHandlerBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public DatabaseHandlerBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public DatabaseHandlerBuilder setConverter(Converter converter) {
            this.converter = converter;
            return this;
        }

        public DatabaseHandlerBuilder setFormatter(DateTimeFormatter FORMATTER) {
            this.FORMATTER = FORMATTER;
            return this;
        }

        public DatabaseHandler build() {
            return new DatabaseHandler(this);
        }
    }
}
