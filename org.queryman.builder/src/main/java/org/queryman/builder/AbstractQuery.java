/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.ast.AbstractSyntaxTree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Standard implementation of {@link Query}.
 *
 * @author Timur Shaidullin
 */
public abstract class AbstractQuery implements Query, AstVisitor {
    private final AbstractSyntaxTree tree;

    public AbstractQuery(AbstractSyntaxTree tree) {
        this.tree = tree;
    }

    private void assembleTree() {
        tree.reinitialize();
        assemble(tree);
    }

    @Override
    public String sql() {
        assembleTree();
        return tree.toString();
    }

    @Override
    public String toString() {
        return sql();
    }

    @Override
    public PreparedStatement buildPreparedStatement(Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("");

        return statement;
    }
}
