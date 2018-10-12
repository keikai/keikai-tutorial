package io.keikai.tutorial.persistence;

import io.keikai.tutorial.Configuration;
import org.hsqldb.cmdline.*;
import org.slf4j.*;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.*;

/**
 * Create adn close a connection for every query.
 */
public class WorkflowDao {
    private static final Logger logger = LoggerFactory.getLogger(WorkflowDao.class);
    static final String TABLE_NAME = "workflow";

    static public void initDatabase() {
        try {
            Class.forName(Configuration.JDBC_DRIVER);
            try (Connection con = createConnection();) {
                initializeTable(con);
                logger.info("-> initialized table " + TABLE_NAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Connection createConnection() {
        try {
            return DriverManager.getConnection(Configuration.HSQLDB_CONNECTION_STRING, "SA", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static private void initializeTable(Connection con) throws IOException, URISyntaxException, SqlToolError, SQLException {
        File inputFile = new File(WorkflowDao.class.getResource("/workflow.sql").toURI());
        SqlFile file = new SqlFile(inputFile);
        file.setConnection(con);
        file.execute();
    }

    synchronized static public void insert(Submission submission) {
        String sql = "INSERT INTO " + TABLE_NAME + " (form, formName, state, lastUpdate, owner) VALUES( ?, ?, ?, ?, ?)";
        try (Connection con = createConnection();
             PreparedStatement statement = con.prepareStatement(sql);
        ) {
            Blob form = con.createBlob();
            form.setBytes(1, submission.getForm().toByteArray());
            statement.setBlob(1, form);
            statement.setString(2, submission.getFormName());
            statement.setString(3, submission.getState().name());
            statement.setTimestamp(4, Timestamp.valueOf(submission.getLastUpdate()));
            statement.setString(5, submission.getOwner());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    synchronized static public List<Submission> queryAll() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        LinkedList<Submission> list = new LinkedList<>();
        try (Connection con = createConnection();
             PreparedStatement statement = con.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                Submission submission = new Submission();
                submission.setId(resultSet.getInt("id"));
                submission.setFormName(resultSet.getString("formName"));
                submission.setOwner(resultSet.getString("owner"));
                submission.setState(Submission.State.valueOf(resultSet.getString("state")));
                submission.setLastUpdate(resultSet.getTimestamp("lastUpdate").toLocalDateTime());
                Blob formBlob = resultSet.getBlob("form");
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                outputStream.write(formBlob.getBytes(1, (int) formBlob.length()));
                submission.setForm(outputStream);
                list.add(submission);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void update(Submission submission) {
        String sql = "UPDATE " + TABLE_NAME + " SET state=?, lastUpdate=? WHERE id=?";
        try (Connection con = createConnection();
             PreparedStatement statement = con.prepareStatement(sql);
        ) {
            statement.setString(1, submission.getState().name());
            statement.setTimestamp(2, Timestamp.valueOf(submission.getLastUpdate()));
            statement.setInt(3, submission.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
