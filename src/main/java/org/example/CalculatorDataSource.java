package org.example;

import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalculatorDataSource {

    private static CalculatorDataSource instance = null;

    private static final String CON_STR = "jdbc:sqlite:myCalc.db";

    private static final Connection CONNECTION;

    public static synchronized CalculatorDataSource getInstance() {
        if (instance == null) {
            instance = new CalculatorDataSource();
        }
        return instance;
    }


    static {
        try {
            DriverManager.registerDriver(new JDBC());
            CONNECTION = DriverManager.getConnection(CON_STR);
            CONNECTION.setAutoCommit(false);
            createTable();
        } catch (SQLException throwables) {
            throw new IllegalStateException(throwables);
        }
    }

    static void  createTable() {
        try {
            CONNECTION.createStatement().execute("CREATE TABLE IF NOT EXISTS Calculations " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "expression TEXT NOT NULL)");
        } catch (SQLException exception) {

        }

    }

    void saveExpression(String text) {
        try {
            CONNECTION.beginRequest();
            Statement stmt = CONNECTION.createStatement();
            ResultSet rSet = stmt.executeQuery(("SELECT COUNT(*) AS total FROM Calculations"));
            int total = rSet.getInt("total");
            if (total == 10) {
                CONNECTION.createStatement().execute("DELETE FROM Calculations WHERE id=(SELECT MIN(id) FROM Calculations)");
            }
            PreparedStatement pstmt = CONNECTION.prepareStatement("INSERT INTO Calculations (expression) VALUES (?)");
            pstmt.setString(1, text);
            pstmt.executeUpdate();
            CONNECTION.commit();
        } catch (SQLException exception) {
            try {
                CONNECTION.rollback();
            } catch (SQLException throwables) {
                exception.printStackTrace();
                throw new IllegalStateException(throwables);
            }
            throw new IllegalStateException(exception);
        }

    }

    List<String> selectAll() {
        List<String > expressions = new ArrayList<>();
        try {
            Statement stmt = CONNECTION.createStatement();
            ResultSet set = stmt.executeQuery("SElECT expression FROM Calculations");
            while (set.next()) {
                expressions.add(set.getString("expression"));
            }

        } catch (SQLException exception) {
            throw new IllegalStateException(exception);
        }
        return expressions;
    }


}
