/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.testing;

import org.queryman.builder.JdbcException;
import org.queryman.builder.PropertiesLoader;
import org.queryman.builder.Query;
import org.queryman.builder.ResourceLoadException;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public class Jdbc {
    private static Properties properties;

    static {
        PropertiesLoader loader = new PropertiesLoader();

        try {
            loader.load();
        } catch (IOException e) {
            throw new ResourceLoadException(e.getMessage());
        }

        properties = loader.getProperties();
    }

    public static void inJdbcByDriverManager(Query query) {
        try(Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            Statement statement = connection.createStatement();
            statement.executeQuery(query.sql());
        } catch (SQLException e) {
            throw new JdbcException(e.getMessage());
        }
    }

    public static void inJdbcByDataSource(Query query) {

        try(Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query.sql();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
