/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Queryman.asList;
import static org.queryman.builder.Queryman.update;

/**
 * @author Timur Shaidullin
 */
public class UpdateTest extends BaseTest  {
    private static Connection connection;

    @BeforeAll
    static void beforeAll() {
        connection = JDBC.createConnection();

//        insertMock(connection);
    }

    @Test
    void complexUpdate() throws SQLException {

        Query query = update("types")
           .set(
              asList("smallint","integer", "bigint", "decimal", "numeric", "real", "double_precision", "boolean"),
              asList(11, 12, 13, 14, 15, 16, 17, false)
           )
           .returning("smallint","integer", "bigint", "decimal", "numeric", "real", "double_precision", "boolean");

        try (Statement statement = connection.createStatement()) {
            statement.execute(query.sql());
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                assertEquals(rs.getInt(1), 11);
                assertEquals(rs.getInt(2), 12);
                assertEquals(rs.getInt(3), 13);
                assertEquals(rs.getInt(4), 14);
                assertEquals(rs.getInt(5), 15);
                assertEquals(rs.getInt(6), 16);
                assertEquals(rs.getInt(7), 17);
                assertEquals(rs.getBoolean(8), false);
            }

        }

        try (PreparedStatement statement = query.buildPreparedStatement(connection)) {
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                assertEquals(rs.getInt(1), 11);
                assertEquals(rs.getInt(2), 12);
                assertEquals(rs.getInt(3), 13);
                assertEquals(rs.getInt(4), 14);
                assertEquals(rs.getInt(5), 15);
                assertEquals(rs.getInt(6), 16);
                assertEquals(rs.getInt(7), 17);
                assertEquals(rs.getBoolean(8), false);
            }
        }
    }
}
