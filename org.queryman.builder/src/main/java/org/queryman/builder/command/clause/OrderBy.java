/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.clause;

import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.token.Expression;

import static org.queryman.builder.PostgreSQL.asName;

/**
 * This {@code class} represents an ORDER BY clause.
 *
 * @author Timur Shaidullin
 */
public final class OrderBy implements AstVisitor {
    private final Expression name;
    private final Expression sorting;
    private final Expression nulls;

    public OrderBy(String name) {
        this(name, null, null);
    }

    public OrderBy(String name, String sorting) {
        this(name, sorting, null);
    }

    public OrderBy(String name, String sorting, String nulls) {
        this(
           asName(name),
           sorting != null ? asName(sorting) : null,
           nulls != null ? asName(nulls) : null
        );
    }

    public OrderBy(Expression name, Expression sorting, Expression nulls) {
        this.name = name;
        this.sorting = sorting;
        this.nulls = nulls;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        tree.addLeaf(name);

        if (sorting != null)
            tree.addLeaf(sorting);

        if (nulls != null)
            tree.addLeaf(nulls);
    }
}
