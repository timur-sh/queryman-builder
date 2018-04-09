/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.ast.TreeFormatterUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Standard implementation of {@link Query}.
 *
 * @author Timur Shaidullin
 */
public abstract class AbstractQuery implements Query, AstVisitor {
    @Override
    public String sql() {
        AbstractSyntaxTree tree = Queryman.getTree();
        assemble(tree);
        return tree.toString();
    }

    @Override
    public String toString() {
        return sql();
    }

    @Override
    public PreparedStatement buildPreparedStatement(Connection conn) throws SQLException {
        AbstractSyntaxTree tree = Queryman.getTree();
        assemble(tree);
        return TreeFormatterUtil.buildPreparedStatement(tree, conn);
    }
}
