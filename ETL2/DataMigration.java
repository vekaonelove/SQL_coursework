package com.example.sqlspringbatch.ETL2;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;

public class DataMigration {
    private static final String OLTP_DB_URL = "jdbc:postgresql://localhost:5432/final";
    private static final String DWH_DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String OLTP_DB_USERNAME = "postgres";
    private static final String OLTP_DB_PASSWORD = "user";
    private static final String DWH_DB_USERNAME = "postgres";
    private static final String DWH_DB_PASSWORD = "user";

    public static void main(String[] args) {
        try {
            Connection sourceConnection = DriverManager.getConnection(OLTP_DB_URL, OLTP_DB_USERNAME, OLTP_DB_PASSWORD);
            Connection destConnection = DriverManager.getConnection(DWH_DB_URL, DWH_DB_USERNAME, DWH_DB_PASSWORD);

            transferData(sourceConnection, destConnection, "dim_country", "country_id", "name");
            transferData(sourceConnection, destConnection, "dim_experience_level", "experience_id", "name");

            transferData(sourceConnection, destConnection, "dim_subject", "subject_id", "name");

            transferDataWithForeignKey(sourceConnection, destConnection, "dim_performer", "performer_id", "experience_id", "subject_id", "name", "surname", "phone_number", "email");

            transferData(sourceConnection, destConnection, "dim_city", "city_id", "name", "country_id");

            transferDataWithForeignKey(sourceConnection, destConnection, "dim_exam", "exam_id", "subject_id", "country_id", "name");

            transferData(sourceConnection, destConnection, "dim_customer", "customer_id", "name",
                    "surname", "phone_number", "email");
            transferDataWithForeignKey(sourceConnection, destConnection, "fact_order", "order_id", "subject_id", "performer_id", "exam_id", "city_id", "customer_id");

            // Close connections
            sourceConnection.close();
            destConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void transferData(Connection sourceConnection, Connection destConnection,
                                     String tableName, String... columnNames) throws SQLException {
        String selectQuery = "SELECT " + String.join(", ", columnNames) + " FROM " + tableName;
        String insertQuery = "INSERT INTO " + tableName + " (" + String.join(", ", columnNames) + ") VALUES (" +
                String.join(", ", new String[columnNames.length]).replaceAll("(?m)$", "?") + ")";

        try (
                PreparedStatement selectStatement = sourceConnection.prepareStatement(selectQuery);
                PreparedStatement insertStatement = destConnection.prepareStatement(insertQuery)
        ) {
            ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                for (int i = 0; i < columnNames.length; i++) {
                    insertStatement.setObject(i + 1, resultSet.getObject(columnNames[i]));
                }
                insertStatement.executeUpdate();
            }
        }


    }
    private JdbcTemplate oltpJdbcTemplate;
    private JdbcTemplate dwhJdbcTemplate;


    private void migrateCities() {
        oltpJdbcTemplate.query("SELECT * FROM City", (rs) -> {
            long cityId = rs.getLong("ID");
            String name = rs.getString("name");
            long countryId = rs.getLong("countryID");

            dwhJdbcTemplate.update("INSERT INTO dim_city (city_id, country_id, name) VALUES (?, ?, ?) ON CONFLICT (city_id) DO NOTHING",
                    cityId, name, countryId);
        });
    }


    private static void transferDataWithForeignKey(Connection sourceConnection, Connection destConnection,
                                                   String tableName, String idColumnName, String... columnNames) throws SQLException {
        String selectQuery = "SELECT " + idColumnName + ", " + String.join(", ", columnNames) + " FROM " + tableName;
        String insertQuery = "INSERT INTO " + tableName + " (" + idColumnName + ", " + String.join(", ", columnNames) +
                ") VALUES (" +
                "?, ".repeat(columnNames.length) + "?)";

        try (
                PreparedStatement selectStatement = sourceConnection.prepareStatement(selectQuery);
                PreparedStatement insertStatement = destConnection.prepareStatement(insertQuery)
        ) {
            ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                insertStatement.setInt(1, resultSet.getInt(idColumnName));
                for (int i = 0; i < columnNames.length; i++) {
                    insertStatement.setObject(i + 2, resultSet.getObject(columnNames[i]));
                }
                insertStatement.executeUpdate();
            }
        }
    }
}