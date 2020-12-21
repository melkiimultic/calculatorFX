package org.example;

import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalculatorDataSource {

    private static final String CON_STR = "jdbc:sqlite:myCalc.db";

    static {
        try {
            DriverManager.registerDriver(new JDBC());
            createTable();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(CON_STR);
            connection.setAutoCommit(false);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    static void createTable() {
        Connection connection = getConnection();
        try {
            connection.beginRequest();
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS Calculations " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "expression TEXT NOT NULL)");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    static void saveExpression(String text) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.beginRequest();
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
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.rollback();
                conn.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    static List<String> selectAll() {
        List<String> expressions = new ArrayList<>();
        Connection connection = null;
        try {
            connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery("SElECT expression FROM Calculations");
            while (set.next()) {
                expressions.add(set.getString("expression"));
            }

        } catch (SQLException exception) {
            throw new IllegalStateException(exception);
        } finally {
            try {
                connection.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }

        return expressions;
    }


//    static void doWithConnection(SqlConsumer consumer) throws SQLException {
//        try (Connection conn = DriverManager.getConnection(CON_STR)) {
//            consumer.consume(conn);
//        }
//    }
//
//    static <T> T returnWithConnection(SqlFunction<T> func) throws SQLException {
//        try (Connection conn = DriverManager.getConnection(CON_STR)) {
//            return func.apply(conn);
//        }
//    }
}

//@FunctionalInterface
//interface SqlConsumer {
//    void consume(Connection connection) throws SQLException;
//}
//
//@FunctionalInterface
//interface SqlFunction<T> {
//    T apply(Connection connection) throws SQLException;
//}
