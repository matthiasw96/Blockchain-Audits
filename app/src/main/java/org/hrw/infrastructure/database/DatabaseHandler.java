package org.hrw.infrastructure.database;

import org.hrw.datamodels.Datastructure;
import org.hrw.datamodels.Mapper;
import org.hrw.datamodels.ServerRecord;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DatabaseHandler {
    private final String hostAddress;
    private final String username;
    private final String password;
    private final Mapper mapper;
    private final DateTimeFormatter FORMATTER;

    public DatabaseHandler(DatabaseHandlerBuilder builder) {
        this.hostAddress = builder.hostAddress;
        this.username = builder.username;
        this.password = builder.password;
        this.FORMATTER = builder.FORMATTER;
        this.mapper = new Mapper();
    }

    public List<ServerRecord> readFromDatabase(long startPoint, long endPoint, String tableName) throws SQLException {
        System.out.println(LocalDateTime.now().format(FORMATTER) + "Reading from database...");

        Statement statement = createStatement();
        String query = createSelectQuery(startPoint, endPoint, tableName);
        ResultSet resultSet = statement.executeQuery(query);
        return this.mapper.resultSetToServerData(resultSet);
    }

    public void writeToDatabase(List<? extends Datastructure> data, String tableName) throws SQLException {
        try {
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Connecting to database...");

            Statement statement = createStatement();
            String query = createInsertQuery(data, tableName);
            statement.executeUpdate(query);

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Data stored");
        } catch (Exception e) {
            System.out.println("Connecting to database failed");
            throw e;
        }
    }

    private Statement createStatement() throws SQLException {
        String url = "jdbc:postgresql://" + hostAddress + ":5432/postgres";
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection.createStatement();
    }

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
                ON CONFLICT ("timestamp") DO NOTHING;""";
    }

    private String createSelectQuery(long startPoint, long endPoint, String tableName){
        return """
                SELECT * FROM\s""" + tableName + """
                \sWHERE "timestamp" >= """ + startPoint + """
                \sAND "timestamp" < """ + endPoint + """
                """;
    }

    public static class DatabaseHandlerBuilder {
        String hostAddress;
        String username;
        String password;
        DateTimeFormatter FORMATTER;

        public DatabaseHandlerBuilder setHostAddress(String hostAddress) {
            this.hostAddress = hostAddress;
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

        public DatabaseHandlerBuilder setFormatter(DateTimeFormatter FORMATTER) {
            this.FORMATTER = FORMATTER;
            return this;
        }

        public DatabaseHandler build() {
            return new DatabaseHandler(this);
        }
    }
}
