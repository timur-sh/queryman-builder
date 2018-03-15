/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import com.zaxxer.hikari.HikariDataSource;
import org.queryman.builder.command.insert.InsertFinalStep;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.queryman.builder.Bootstrap.BOOT;
import static org.queryman.builder.Queryman.asConstant;
import static org.queryman.builder.Queryman.insertInto;

/**
 * @author Timur Shaidullin
 */
public class BaseTest {
    private static FlywayManager manager = new FlywayManager();
    private final static HikariDataSource dataSource = BOOT.getDataSource();

    static {
        manager.init();
        manager.clean();
        manager.migrate();
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

    protected static void inBothStatement(Query query, TestResultSet<ResultSet> test) throws SQLException {
        inStatement(query, test);
        inPreparedStatement(query, test);
    }

    protected static void inPreparedStatement(Query query, TestResultSet<ResultSet> test) throws SQLException {
        try (Statement statement = dataSource.getConnection().createStatement()) {
            statement.execute(query.sql());
            try (ResultSet rs = statement.getResultSet()) {
                if (rs != null)
                    while (rs.next())
                        test.doIt(rs);
            }
        }
    }

    protected static void inStatement(Query query, TestResultSet<ResultSet> test) throws SQLException {
        try (PreparedStatement statement = query.buildPreparedStatement(dataSource.getConnection())) {
            statement.execute();
            try (ResultSet rs = statement.getResultSet()) {
                if (rs != null)
                    while (rs.next())
                        test.doIt(rs);
            }
        }
    }

    public interface TestResultSet<T> {
        void doIt(T rs) throws SQLException;
    }
}
