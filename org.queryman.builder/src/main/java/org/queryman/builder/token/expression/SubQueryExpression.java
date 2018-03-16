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
import org.queryman.builder.token.PreparedExpression;

import java.util.Map;

import static org.queryman.builder.Queryman.getTree;
import static org.queryman.builder.ast.TreeFormatterUtil.buildPreparedParameters;
import static org.queryman.builder.ast.TreeFormatterUtil.buildPreparedSQL;

/**
 * Represent a subquery expression.
 * <code>
 *     Queryman.asSubQuery(select("price")); // (SELECT price)
 *     Queryman.asSubQuery(select("max(price)")).as("max"); // (SELECT price) AS max
 *  </code>
 *
 * @author Timur Shaidullin
 */
public class SubQueryExpression<T> extends PreparedExpression {
    private Expression[] arr;
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

    @Override
    public String getPlaceholder() {
        return "(" + buildPreparedSQL(query) + ")";
    }

    @Override
    public Object getValue() {
        // Method must not be called because it contains list of other
        // expressions those must be bound using #bind(Map) method
        throw new IllegalStateException("Method must not be called");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void bind(Map map) {

        buildPreparedParameters(query).values().stream()
           .filter(v -> v instanceof PreparedExpression)
           .forEach(v -> {
               map.put(map.size() + 1, v);
           });
    }
}
