/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.ast.ASTBuilder;
import org.queryman.builder.ast.AbstractSyntaxTree;

/**
 * @author Timur Shaidullin
 */
public abstract class AbstractQuery implements Query, ASTBuilder {
    private final AbstractSyntaxTree tree;

    public AbstractQuery(AbstractSyntaxTree tree) {
        this.tree = tree;
    }

    @Override
    public String sql() {
        buildTree(tree);
        return tree.toString();
    }

    @Override
    public String toString() {
        return sql();
    }
}
