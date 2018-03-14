/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.queryman.builder.command.insert.InsertFinalStep;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Queryman.asConstant;
import static org.queryman.builder.Queryman.conditionAny;
import static org.queryman.builder.Queryman.insertInto;
import static org.queryman.builder.Queryman.select;

/**
 * @author Timur Shaidullin
 */
public class PostgreSQLSelectTest extends BaseTest  {
    private static Connection connection;

    @BeforeAll
    static void beforeAll() {
        connection = JDBC.createConnection();


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
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                System.out.println(rs.getInt(1));
                System.out.println(rs.getInt(2));
                System.out.println(rs.getInt(3));
            }
        }

        try (PreparedStatement statement = select.buildPreparedStatement(connection)) {
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                System.out.println(rs.getInt(1));
                System.out.println(rs.getInt(2));
                System.out.println(rs.getInt(3));
            }

        }
    }
}
