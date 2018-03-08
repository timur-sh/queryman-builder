/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Timur Shaidullin
 */
public class TreeFormatterUtil {
    /**
     * Builds a SQL string.
     *
     * @param tree abstract syntax tree.
     * @return SQL string
     */
    static String getSQL(AbstractSyntaxTree tree) {
        String sql = new TreeFormatter().buildSQL(tree.getRootNode());
        //todo move to the logger
        System.out.println(sql);
        return sql;
    }

    /**
     * Builds a prepared statement and binds attributes of tree to it.
     *
     * @param tree abstract syntax tree
     * @param conn connection is used in creation a prepared statement
     * @return prepared statement
     *
     * @throws SQLException may be thrown during creation a prepared statement
     */
    public static PreparedStatement buildPreparedStatement(AbstractSyntaxTree tree, Connection conn)
       throws SQLException {

        TreeFormatter formatter = new TreeFormatter();
        String sql = formatter.buildSQL(tree.getRootNode(), true);
        //todo move to the logger
        System.out.println(sql);

        PreparedStatement statement = conn.prepareStatement(sql);

        JavaTypeToJdbc mapping = new JavaTypeToJdbc(conn, statement);
        return mapping.bind(formatter.getParameters());
    }
}
