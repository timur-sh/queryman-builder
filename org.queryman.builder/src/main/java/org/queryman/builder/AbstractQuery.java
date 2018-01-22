/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.ast.AST;

/**
 * @author Timur Shaidullin
 */
public abstract class AbstractQuery implements Query, ASTBuilder {
    private final AST ast;

    public AbstractQuery(AST ast) {
        this.ast = ast;
    }

    @Override
    public String sql() {

        return null;
    }

    @Override
    public String toString() {
        return sql();
    }

    private void buildTree() {

    }
}
