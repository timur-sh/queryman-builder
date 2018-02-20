/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token.expression;

import org.queryman.builder.Query;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.token.Expression;

import static org.queryman.builder.PostgreSQL.getTree;

/**
 * Represent a subquery expression.
 * <code>
 *     PostgreSQL.asSubQuery(select("price")); // (SELECT price)
 *     PostgreSQL.asSubQuery(select("max(price)")).as("max"); // (SELECT price) AS max
 *  </code>
 *
 * @author Timur Shaidullin
 */
public class SubQueryExpression extends Expression {
    private final Query query;

    public SubQueryExpression(Query query) {
        super("");
        this.query = query;
    }

    /**
     * @return a string representation of query
     */
    @Override
    protected String prepareName() {
        AbstractSyntaxTree tree = getTree();
        query.assemble(tree);

        return "(" + tree.toString() + ")";
    }

    public Query getQuery() {
        return query;
    }
}
