package io.keikai.tutorial;

import org.hsqldb.cmdline.*;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.*;

public class SampleDataDao {
    /**
     * http://hsqldb.org/doc/guide/dbproperties-chapt.html
     * shutdown=true, Automatic Shutdown, shut down the database when the last connection is closed
     */
    public static final String HSQLDB_CONNECTION_STRING = "jdbc:hsqldb:file:database/tutorial;shutdown=true";
    static String TABLE_NAME = "tutorial";
    static private Connection con;
    static Statement stmt;

    public static void main(String[] args) {
        try {
            con = DriverManager.getConnection(HSQLDB_CONNECTION_STRING, "SA", "");
            stmt = con.createStatement();
            executeSqlFile();
            queryAll();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void initDatabase() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            if (con == null) {
                con = DriverManager.getConnection(HSQLDB_CONNECTION_STRING, "SA", "");
            }
            stmt = con.createStatement();
            executeSqlFile();
            System.out.println("-> initialized the database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void close() {
        try {
            con.close();
            stmt.close();
            System.out.println("-> close the connection");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static private void executeSqlFile() throws IOException, URISyntaxException, SqlToolError, SQLException {
        File inputFile = new File(SampleDataDao.class.getResource("/tutorial.sql").toURI());
        SqlFile file = new SqlFile(inputFile);
        file.setConnection(con);
        file.execute();
    }

    static public List<Expense> queryAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME;
        ResultSet resultSet = stmt.executeQuery(sql);
        LinkedList<Expense> list = new LinkedList<>();
        while (resultSet.next()) {
            Expense expense = new Expense();
            expense.setId(resultSet.getInt("id"));
            expense.setCategory(resultSet.getString("category"));
            expense.setQuantity(resultSet.getInt("quantity"));
            expense.setSubtotal(resultSet.getInt("subtotal"));
            list.add(expense);
        }
        return list;
    }

    static public List<Expense> queryByCategory() {
        String sql = "SELECT category, sum(quantity) as quantity, sum(subtotal) as subtotal FROM " + TABLE_NAME + " GROUP BY category";
        LinkedList<Expense> list = new LinkedList<>();
        try {
            ResultSet resultSet = null;
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                Expense expense = new Expense();
                expense.setCategory(resultSet.getString("category"));
                expense.setQuantity(resultSet.getInt("quantity"));
                expense.setSubtotal(resultSet.getInt("subtotal"));
                list.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    static public void insert(Expense expense) {
        try {
            String sql = "INSERT INTO " + TABLE_NAME + " (category, quantity, subtotal) VALUES( ?, ?, ?)";
            PreparedStatement statement = null;
            statement = con.prepareStatement(sql);
            statement.setString(1, expense.getCategory());
            statement.setInt(2, expense.getQuantity());
            statement.setInt(3, expense.getSubtotal());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
