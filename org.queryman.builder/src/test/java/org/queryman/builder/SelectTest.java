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
import static org.queryman.builder.Queryman.select;

/**
 * @author Timur Shaidullin
 */
public class SelectTest extends BaseTest  {
    private static Connection connection;

    @BeforeAll
    static void beforeAll() {
        connection = JDBC.createConnection();

        insertMock(connection);
    }

    @Test
    void complexSelectTest() throws SQLException {
        Query select = select(1, 2, "integer")
           .from("types")
           .where("bigint", "=", 3)
           .and("smallint", "IS NOT", null)
//           .and(conditionAny("bigint", ))
           ;

        try (Statement statement = connection.createStatement()) {
            statement.execute(select.sql());
            try (ResultSet rs = statement.getResultSet()) {
                while (rs.next()) {
                    assertEquals(1, rs.getInt(1));
                    assertEquals(2, rs.getInt(2));
                    assertEquals(2, rs.getInt(3));
                }
            }
        }

        try (PreparedStatement statement = select.buildPreparedStatement(connection)) {
            statement.executeQuery();
            try (ResultSet rs = statement.getResultSet()) {
                while (rs.next()) {
                    assertEquals(1, rs.getInt(1));
                    assertEquals(2, rs.getInt(2));
                    assertEquals(2, rs.getInt(3));
                }
            }

        }
    }
}
