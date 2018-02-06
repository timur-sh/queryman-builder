/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.testing;

import org.queryman.builder.Bootstrap;
import org.queryman.builder.JDBCException;
import org.queryman.builder.JdbcException;
import org.queryman.builder.Query;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Timur Shaidullin
 */
public class JDBC {
    private static DataSource dataSource = new Bootstrap().init().getDataSource();
    private static Connection connection;

    public void createConnection() {
        if (connection != null)
            closeConnection();

        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new JDBCException(e.getMessage());
        }
    }

    public void closeConnection() {
        if (connection == null)
            return;

        try {
            connection.close();
        } catch (SQLException e) {

            throw new JDBCException(e.getMessage());
        }
    }

    public static void inJdbc(Query query) {
        try (Statement statement = connection.createStatement()) {
            statement.executeQuery(query.sql());
        } catch (SQLException e) {
            throw new JdbcException(e.getMessage());
        }
    }
}
