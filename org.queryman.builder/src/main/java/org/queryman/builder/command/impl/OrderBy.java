/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.PostgreSQL;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.ast.NodesMetadata;
import org.queryman.builder.token.Expression;

import static org.queryman.builder.PostgreSQL.asConstant;

/**
 * @author Timur Shaidullin
 */
final class OrderBy implements AstVisitor {
    private final Expression name;
    private final Expression sorting;
    private final Expression nulls;

    OrderBy(String name) {
        this(name, null, null);
    }

    OrderBy(String name, String sorting) {
        this(name, sorting, null);
    }

    OrderBy(String name, String sorting, String nulls) {
        this(
           asConstant(name),
           sorting != null ? asConstant(sorting) : null,
           nulls != null ? asConstant(nulls) : null
        );
    }

    OrderBy(Expression name, Expression sorting, Expression nulls) {
        this.name = name;
        this.sorting = sorting;
        this.nulls = nulls;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        tree.startNode(NodesMetadata.EMPTY)
           .addLeaf(name);

        if (sorting != null)
            tree.addLeaf(sorting);

        if (nulls != null)
            tree.addLeaf(nulls);

        tree.endNode();
    }
}
