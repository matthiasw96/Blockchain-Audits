package org.hrw.infrastructure.database;

import org.hrw.datamodels.Datastructure;
import org.hrw.datamodels.ServerData;
import org.hrw.infrastructure.collector.Collector;
import org.hrw.logging.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DatabaseHandler {
    private final String hostAddress;
    private final int port;
    private final String databaseName;
    private final String username;
    private final String password;
    private final Logger logger;
    private final DateTimeFormatter FORMATTER;

    public DatabaseHandler(DatabaseHandlerBuilder builder) {
        this.hostAddress = builder.hostAddress;
        this.port = builder.port;
        this.databaseName = builder.databaseName;
        this.username = builder.username;
        this.password = builder.password;
        this.logger = builder.logger;
        this.FORMATTER = builder.FORMATTER;
    }

    public List<ServerData> readFromDatabase(long startPoint, long endPoint, String tableName) throws SQLException {
        System.out.println("Reading from database...");
        Statement statement = this.createStatement();
        String query = this.createSelectQuery(startPoint, endPoint, tableName);
        ResultSet resultSet = statement.executeQuery(query);
        return this.createServerData(resultSet);
    }

    private List<ServerData> createServerData(ResultSet resultSet) throws SQLException {
        List<ServerData> serverData = new ArrayList<>();
        List<String> columnNames = this.getColumnNames(resultSet);

        while (resultSet.next()) {
            Map<String,String> map = this.createMap(columnNames, resultSet);
            serverData.add(new ServerData(map));
        }
        return serverData;
    }

    private Map<String, String> createMap(List<String> columnNames, ResultSet resultSet) throws SQLException {
        Map<String, String> map = new HashMap<>();

        for(String columnName : columnNames) {
            map.put(columnName, resultSet.getString(columnName));
        }

        return map;
    }

    private List<String> getColumnNames(ResultSet resultSet) throws SQLException {
        List<String> columnNames = new ArrayList<>();

        for(int i=1; i<resultSet.getMetaData().getColumnCount(); i++) {
            columnNames.add(resultSet.getMetaData().getColumnName(i));
        }

        return columnNames;
    }

    public void writeToDatabase(List<Datastructure> data, UUID jobId, String tableName) throws SQLException {
        try {
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Connecting to database...");
            Statement statement = this.createStatement();
            String query = this.createInsertQuery(data, tableName);

            statement.executeUpdate(query);

            logger.log(jobId, Logger.Stage.DB, Logger.Status.OK, "Persisted",null);
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Data stored");
        } catch (Exception e) {
            logger.log(jobId, Logger.Stage.DB, Logger.Status.FAIL, "Error", Map.of("error", e.toString()));
            System.out.println("Connecting to database failed");
            throw e;
        }
    }

    private Statement createStatement() throws SQLException {
        String url = "jdbc:postgresql://" + hostAddress + ":" + port + "/" + databaseName;
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection.createStatement();
    }

    private String createInsertQuery(List<Datastructure> data, String tableName) {
        String column_names = data.getFirst().getAttributeNames();
        String values ="";

        for(int i=0;i<data.size();i++) {
            if(i == data.size()-1) {
                values+="('" + data.get(i).getJobId() + "'," + data.get(i).toString() + ")\n";
            } else {
                values+="('" + data.get(i).getJobId() + "'," + data.get(i).toString() + "),\n";
            }
        }

        return """
                INSERT INTO\s""" + tableName + """
                 (
                """ + column_names + """
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
        int port;
        String databaseName;
        String username;
        String password;
        Logger logger;
        DateTimeFormatter FORMATTER;

        public DatabaseHandlerBuilder setHostAddress(String hostAddress) {
            this.hostAddress = hostAddress;
            return this;
        }

        public DatabaseHandlerBuilder setPort(int port) {
            this.port = port;
            return this;
        }

        public DatabaseHandlerBuilder setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
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

        public DatabaseHandlerBuilder setLogger(Logger logger) {
            this.logger = logger;
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
