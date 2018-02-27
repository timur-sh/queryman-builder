/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Timur Shaidullin
 */
public class JdbcTest extends BaseTest {
    @Test
    void prepareStatementTest() throws SQLException {
        Connection connection = JDBC.createConnection();

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"user\" WHERE name = ?");

        statement.setString(1, "Timur");
        ResultSet set = statement.executeQuery();


        connection.close();
    }
}
