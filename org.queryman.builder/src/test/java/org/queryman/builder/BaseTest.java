/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.queryman.builder.command.insert.InsertFinalStep;
import org.queryman.builder.testing.JDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.queryman.builder.Queryman.asConstant;
import static org.queryman.builder.Queryman.insertInto;

/**
 * @author Timur Shaidullin
 */
public class BaseTest {
    private static FlywayManager manager = new FlywayManager();

    protected static final org.queryman.builder.testing.JDBC JDBC = new JDBC();

    static {
        manager.init();
        manager.clean();
        manager.migrate();
    }

    @BeforeAll
    static void beforeAll() {
        JDBC.createConnection();
    }

    @AfterAll
    static void afterAll() {
        JDBC.closeConnection();
    }

    protected static void insertMock(Connection connection) {
        short   s1 = 1;
        int     s2 = 2;
        long    s3 = 3;
        double  s4 = 4;
        float   s5 = 5;
        float   s6 = 6;
        double  s7 = 7;
        boolean s8 = true;

        InsertFinalStep insert = insertInto("types")
           .columns(
              "smallint",
              "integer",
              "bigint",
              "decimal",
              "numeric",
              "real",
              "double_precision",
              "boolean"
           )
           .values(
              asConstant(s1),
              asConstant(s2),
              asConstant(s3),
              asConstant(s4),
              asConstant(s5),
              asConstant(s6),
              asConstant(s7),
              asConstant(s8)
           )
           .returning("*");

        try (Statement statement = connection.createStatement()) {
            statement.execute(insert.sql());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
