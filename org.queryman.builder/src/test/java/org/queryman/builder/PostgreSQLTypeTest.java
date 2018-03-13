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
import org.queryman.builder.utils.ArrayUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Queryman.asConstant;
import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.Queryman.insertInto;

/**
 * @author Timur Shaidullin
 */
public class PostgreSQLTypeTest extends BaseTest {
    private static Connection connection;

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
              asConstant(1),
              asConstant('2'),
              asConstant("adasdas")
           )
           .returning("*");

        try (Statement statement = connection.createStatement()) {
            statement.execute(insert.sql());

            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                assertEquals("1", rs.getString("varchar"));
                assertEquals("2", rs.getString("char"));
                assertEquals("adasdas", rs.getString("text"));
            }
        }

        try (PreparedStatement statement = insert.buildPreparedStatement(connection)) {
            statement.execute();
        }
    }

    @Test
    void dateTest() throws SQLException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(System.currentTimeMillis());
        Time time = new Time(System.currentTimeMillis());
        String interval = "3 years 2 mons 02:03:00";

        InsertFinalStep insert = insertInto("types")
           .columns(
              "timestamp",
              "date",
              "time",
              "interval",
              "integer"
           )
           .values(
              asConstant(timestamp),
              asConstant(date),
              asConstant(time),
              asConstant(interval).cast("interval"),
              null
           )
           .returning("timestamp", "date", "time", asName("interval").as("i"), "integer");

        try (Statement statement = connection.createStatement()) {
            statement.execute(insert.sql());

            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                assertEquals(date.toString(), rs.getDate("date").toString());
                assertEquals(time.toString(), rs.getTime("time").toString());
                assertEquals(timestamp.toString(), rs.getTimestamp("timestamp").toString());
                assertEquals(interval, rs.getString("i"));
            }
        }

        try (PreparedStatement statement = insert.buildPreparedStatement(connection)) {
            statement.execute();

            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                assertEquals(date.toString(), rs.getDate("date").toString());
                assertEquals(time.toString(), rs.getTime("time").toString());
                assertEquals(timestamp.toString(), rs.getTimestamp("timestamp").toString());
                assertEquals(interval, rs.getString("i"));
            }
        }
    }

    @Test
    void netTest() throws SQLException {
        String cidr = "192.168.1.5/32";
        String inet = "192.168.100.128/25";
        String macaddr = "08:00:2b:01:02:03";

        InsertFinalStep insert = insertInto("types")
           .columns(
              "cidr",
              "inet",
              "macaddr"
           )
           .values(
              asConstant(cidr).cast("cidr"),
              asConstant(inet).cast("inet"),
              asConstant(macaddr).cast("macaddr")
           )
           .returning("*");

        try (Statement statement = connection.createStatement()) {
            statement.execute(insert.sql());

            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                assertEquals(cidr, rs.getString("cidr"));
                assertEquals(inet, rs.getString("inet"));
                assertEquals(macaddr, rs.getString("macaddr"));
            }
        }

        try (PreparedStatement statement = insert.buildPreparedStatement(connection)) {
            statement.execute();

            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                assertEquals(cidr, rs.getString("cidr"));
                assertEquals(inet, rs.getString("inet"));
                assertEquals(macaddr, rs.getString("macaddr"));
            }
        }
    }

    @Test
    void bitTest() throws SQLException {
        String bit = "1";
        String bitVarying = "101";

        InsertFinalStep insert = insertInto("types")
           .columns(
              "bit",
              "bit_varying"
           )
           .values(
              asConstant(bit).cast("bit"),
              asConstant(bitVarying).cast("bit varying")
           )
           .returning("*");

        try (Statement statement = connection.createStatement()) {
            statement.execute(insert.sql());

            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                assertEquals(bit, rs.getString("bit"));
                assertEquals(bitVarying, rs.getString("bit_varying"));
            }
        }

        try (PreparedStatement statement = insert.buildPreparedStatement(connection)) {
            statement.execute();

            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                assertEquals(bit, rs.getString("bit"));
                assertEquals(bitVarying, rs.getString("bit_varying"));
            }
        }
    }

    @Test
    void otherTest() throws SQLException {
        UUID uuid = UUID.randomUUID();
        String xml = "<foo>bar</foo>";
        int[] arr = {1, 2, 3};

        InsertFinalStep insert = insertInto("types")
           .columns(
              "uuid",
              "xml",
              "arr_int"
           )
           .values(
              asConstant(uuid),
              asConstant(xml).cast("xml"),
              asConstant(arr)
           )
           .returning("*");

        try (Statement statement = connection.createStatement()) {
            statement.execute(insert.sql());

            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                assertEquals(uuid, rs.getObject("uuid"));
                assertEquals(xml, rs.getString("xml"));
                assertArrayEquals(ArrayUtils.toWrapper(arr), (Integer[])rs.getArray("arr_int").getArray());
            }
        }

        try (PreparedStatement statement = insert.buildPreparedStatement(connection)) {
            statement.execute();

            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                assertEquals(uuid, rs.getObject("uuid"));
                assertEquals(xml, rs.getString("xml"));
                assertArrayEquals(ArrayUtils.toWrapper(arr), (Integer[])rs.getArray("arr_int").getArray());
            }
        }
    }
}
