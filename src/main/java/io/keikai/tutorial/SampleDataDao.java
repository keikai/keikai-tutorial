package io.keikai.tutorial;

import org.hsqldb.cmdline.*;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.*;

public class SampleDataDao {
    static String TABLE_NAME = "tutorial";
    static private Connection con;
    static Statement stmt;

    public static void main(String[] args) {
        try {
            con = DriverManager.getConnection("jdbc:hsqldb:file:database/demo", "SA", "");
            stmt = con.createStatement();
            executeSqlFile();
            queryAll();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void initDatabase(){
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:file:database/demo", "SA", "");
            stmt = con.createStatement();
            executeSqlFile();
            System.out.println("-> initialized the database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void close(){
        try {
            con.close();
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

    private static void queryAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME;
        ResultSet resultSet = stmt.executeQuery(sql);
        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id") + " | " +
                    resultSet.getString("category") + " | " +
                    resultSet.getInt("quantity")  + " | " +
                    resultSet.getInt("subtotal"));
        }
    }

}
