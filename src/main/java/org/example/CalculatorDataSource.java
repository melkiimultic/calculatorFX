package org.example;

import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalculatorDataSource {

    private static final String DEFAULT_DB_URL = "jdbc:sqlite:myCalc.db";

    static {
        try {
            DriverManager.registerDriver(new JDBC());
            createTable();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Connection getConnection() throws SQLException {
        String dbUrl = System.getProperty("DB_URL");
        if (dbUrl == null) {
            dbUrl = DEFAULT_DB_URL;
        }
        Connection connection = DriverManager.getConnection(dbUrl);
        connection.setAutoCommit(false);
        return connection;
    }

    static void createTable() {
        doWithConnection(conn -> conn.createStatement()
                .execute("CREATE TABLE IF NOT EXISTS Calculations " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "expression TEXT NOT NULL)"));
    }

    static void saveExpression(String text) {
        doWithConnection(
                conn -> {
                    Statement stmt = conn.createStatement();
                    ResultSet rSet = stmt.executeQuery(("SELECT COUNT(*) AS total FROM Calculations"));
                    int total = rSet.getInt("total");
                    if (total == 10) {
                        conn.createStatement().execute("DELETE FROM Calculations WHERE id=(SELECT MIN(id) FROM Calculations)");
                    }
                    stmt.close();
                    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Calculations (expression) VALUES (?)");
                    pstmt.setString(1, text);
                    pstmt.executeUpdate();
                }
        );
    }

    static List<String> selectAll() {
        List<String> expressions = new ArrayList<>();
        Connection connection = null;
        try {
            connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery("SELECT expression FROM Calculations");
            while (set.next()) {
                expressions.add(set.getString("expression"));
            }
        } catch (SQLException exception) {
            throw new IllegalStateException(exception);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return expressions;
    }

    private static void doWithConnection(SqlConsumer connectionConsumer) {
        Connection connection = null;
        try {
            connection = getConnection();
            connectionConsumer.consume(connection);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    @FunctionalInterface
    interface SqlConsumer {
        void consume(Connection conn) throws SQLException;
    }

}
