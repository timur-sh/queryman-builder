/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.queryman.builder.Query;
import org.queryman.builder.Queryman;
import org.queryman.builder.token.PreparedExpression;
import org.queryman.builder.token.expression.prepared.ArrayExpression;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Timur Shaidullin
 */
public class TreeFormatterUtil {
    private final static Logger LOG = LogManager.getLogger("org.queryman.builder.ast");

    /**
     * Builds a SQL string.
     *
     * @param tree abstract syntax tree.
     * @return SQL string
     */
    static String getSQL(AbstractSyntaxTree tree) {
        String sql = new TreeFormatter().buildSQL(tree.getRootNode());

        LOG.info(sql);

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
        Map<Integer, PreparedExpression> params = formatter.getParameters();

        LOG.info(sql);
        if (!params.isEmpty() && LOG.isEnabled(Level.DEBUG)) {
            StringBuilder builder = new StringBuilder();
            builder.append("Parameters\n{");

            SortedSet<Integer> sorted = new TreeSet<>(params.keySet());
            for (int key : sorted) {
                if (params.get(key) instanceof ArrayExpression)
                    builder.append(String.format("\n\t%d -> %s", key, String.valueOf(params.get(key))));
                else
                    builder.append(String.format("\n\t%d -> %s", key, String.valueOf(params.get(key).getValue())));
            }
            builder.append("\n}");

            LOG.debug(builder.toString());
        }

        PreparedStatement statement = conn.prepareStatement(sql);

        JavaTypeToJdbc mapping = new JavaTypeToJdbc(conn, statement);
        return mapping.bind(formatter.getParameters());
    }

    public static String buildPreparedSQL(Query query) {
        AbstractSyntaxTree tree = Queryman.getTree();
        query.assemble(tree);

        TreeFormatter formatter = new TreeFormatter();
        return formatter.buildSQL(tree.getRootNode(), true);
    }

    public static Map<Integer, PreparedExpression> buildPreparedParameters(Query query) {
        AbstractSyntaxTree tree = Queryman.getTree();
        query.assemble(tree);

        TreeFormatter formatter = new TreeFormatter();
        formatter.buildSQL(tree.getRootNode(), true);
        return formatter.getParameters();
    }
}
