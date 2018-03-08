/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.queryman.builder.command.insert.InsertFinalStep;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.insertInto;

/**
 * @author Timur Shaidullin
 */
public class PostgreSQLTypeTest extends BaseTest {
    static Connection connection;

    @BeforeAll
    static void beforeAll() {
        connection = JDBC.createConnection();
    }

    @Test
    void numericTest() throws SQLException {
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
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                assertEquals(s1, rs.getShort("smallint"));
                assertEquals(s2, rs.getInt("integer"));
                assertEquals(s3, rs.getLong("bigint"));
                assertEquals(s4, rs.getFloat("decimal"));
                assertEquals(s5, rs.getFloat("numeric"));
                assertEquals(s6, rs.getFloat("real"));
                assertEquals(s7, rs.getDouble("double_precision"));
                assertEquals(s8, rs.getBoolean("boolean"));
            }
        }

        try (PreparedStatement statement = insert.buildPreparedStatement(connection)) {
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                assertEquals(s1, rs.getShort("smallint"));
                assertEquals(s2, rs.getInt("integer"));
                assertEquals(s3, rs.getLong("bigint"));
                assertEquals(s4, rs.getFloat("decimal"));
                assertEquals(s5, rs.getFloat("numeric"));
                assertEquals(s6, rs.getFloat("real"));
                assertEquals(s7, rs.getDouble("double_precision"));
                assertEquals(s8, rs.getBoolean("boolean"));
            }
        }
    }

    @Test
    void stringTest() throws SQLException {
        InsertFinalStep insert = insertInto("types")
           .columns(
              "varchar",
              "char",
              "text"
           )
           .values(
              asConstant(1).cast("varchar"),
              asConstant('2').cast("char"),
              asConstant("adasdas").cast("text")
           );

        try (Statement statement = connection.createStatement()) {
            statement.execute(insert.sql());
        }

        try (PreparedStatement statement = insert.buildPreparedStatement(connection)) {
            statement.execute();
        }
    }

    @Test
    void dateTest() throws SQLException {
        InsertFinalStep insert = insertInto("types")
           .columns(
              "timestamp",
              "date",
              "time",
              "interval"
           )
           .values(
              asConstant(new Timestamp(System.currentTimeMillis())),
              asConstant(new Date(System.currentTimeMillis())),
              asConstant(new Time(System.currentTimeMillis())),
              null
           );

        try (Statement statement = connection.createStatement()) {
            statement.execute(insert.sql());
        }

        try (PreparedStatement statement = insert.buildPreparedStatement(connection)) {
            statement.execute();
        }
    }

    @Test
    void netTest() throws SQLException {
        InsertFinalStep insert = insertInto("types")
           .columns(
              "cidr",
              "inet",
              "macaddr"
           )
           .values(
//              asConstant(1),
//              asConstant(2),
           );

        try (Statement statement = connection.createStatement()) {
            statement.execute(insert.sql());
        }

        try (PreparedStatement statement = insert.buildPreparedStatement(connection)) {
//            statement
        }
    }

    @Test
    void bitTest() throws SQLException {
        InsertFinalStep insert = insertInto("types")
           .columns(
              "bit",
              "bit_varying"
           )
           .values(
//              asConstant(1),
//              asConstant(2),
           );

        try (Statement statement = connection.createStatement()) {
            statement.execute(insert.sql());
        }

        try (PreparedStatement statement = insert.buildPreparedStatement(connection)) {
//            statement
        }
    }

    @Test
    void otherTest() throws SQLException {
        InsertFinalStep insert = insertInto("types")
           .columns(
              "uuid",
              "xml",
              "arr_int"
           )
           .values(
//              asConstant(1),
//              asConstant(2),
           );

        try (Statement statement = connection.createStatement()) {
            statement.execute(insert.sql());
        }

        try (PreparedStatement statement = insert.buildPreparedStatement(connection)) {
//            statement
        }
    }
}
