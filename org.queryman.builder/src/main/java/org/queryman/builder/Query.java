/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.ast.AstVisitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * It represents a query object allowing either to build a SQL query string,
 * or to prepare a {@link PreparedStatement} object.
 *
 * @author Timur Shaidullin
 */
public interface Query extends AstVisitor {
    /**
     * Builds a SQL string from a query object.
     *
     * @return SQL as string
     */
    String sql();

    /**
     * Builds a prepare statement, including into it a SQL string and values.
     *
     * @param conn connection
     * @return a completed prepared statement contains a SQL string and
     *  necessaries values
     */
    PreparedStatement buildPreparedStatement(Connection conn) throws SQLException;
}
