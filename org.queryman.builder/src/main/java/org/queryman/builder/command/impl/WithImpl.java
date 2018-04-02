/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.Query;
import org.queryman.builder.Queryman;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.command.select.SelectFromStep;
import org.queryman.builder.command.with.SelectFirstStep;
import org.queryman.builder.command.with.WithAsManySteps;
import org.queryman.builder.command.with.WithAsStep;
import org.queryman.builder.token.Expression;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.Queryman.asSubQuery;
import static org.queryman.builder.ast.NodesMetadata.AS;
import static org.queryman.builder.ast.NodesMetadata.EMPTY;
import static org.queryman.builder.ast.NodesMetadata.EMPTY_GROUPED;
import static org.queryman.builder.ast.NodesMetadata.WITH;
import static org.queryman.builder.ast.NodesMetadata.WITH_RECURSIVE;
import static org.queryman.builder.utils.ArrayUtils.toExpressions;

/**
 * @author Timur Shaidullin
 */
public class WithImpl implements
   AstVisitor,
   WithAsStep,
   WithAsManySteps,
   SelectFirstStep {

    private final boolean recursive;
    private final Deque<WithQuery> withQueries = new ArrayDeque<>();

    public WithImpl(String name, String... columns) {
        this(name, false, columns);
    }

    public WithImpl(String name, boolean recursive, String... columns) {
        this.recursive = recursive;
        withQueries.add(new WithQuery(asName(name), toExpressions(columns)));
    }

    @Override
    public final WithImpl as(Query query) {
        return as(asSubQuery(query));
    }

    @Override
    public final WithImpl as(Expression query) {
        withQueries.peekLast().setQuery(query);
        return this;
    }

    @Override
    public WithImpl with(String name, String... columns) {
        withQueries.add(new WithQuery(asName(name), toExpressions(columns)));
        return this;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        if (recursive)
            tree.startNode(WITH_RECURSIVE.setJoinNodes(true), ", ");
        else
            tree.startNode(WITH.setJoinNodes(true), ", ");

        for (WithQuery withQuery : withQueries) {
            tree.startNode(EMPTY)
               .addLeaf(withQuery.name);

            if (withQuery.columns != null)
                tree.startNode(EMPTY_GROUPED, ", ")
                   .addLeaves(withQuery.columns)
                   .endNode();

            if (withQuery.query != null)
                tree.startNode(AS)
                   .addLeaf(withQuery.query)
                   .endNode();

            tree.endNode();
        }

        tree.endNode();
    }

    private SelectImpl initSelect(Expression... columns) {
        SelectImpl select = new SelectImpl(columns);
        select.setWith(this);
        return select;
    }

    /**
     * SELECT statement.
     * Example:
     * <code>
     * select("id", "name", 3); // SELECT id, name, 3
     * </code>
     *
     * @param columns output columns
     * @return select from step
     */
    @SafeVarargs
    public final <T> SelectFromStep select(T... columns) {
        return select(Arrays.stream(columns)
           .map(v -> asName(String.valueOf(v)))
           .toArray(Expression[]::new)
        );
    }

    /**
     * SELECT statement.
     * Example:
     * <code>
     * select(asName("id"), asName("name")); // SELECT id, name
     * </code>
     *
     * @param columns output columns
     * @return select from step
     */
    public final SelectFromStep select(Expression... columns) {
        return initSelect(columns);
    }

    /**
     * SELECT ALL statement.
     * Example:
     * <code>
     * selectAll("id", "name"); // SELECT ALL id, name
     * </code>
     *
     * @param columns output columns
     * @return select from step
     */
    @SafeVarargs
    public final <T> SelectFromStep selectAll(T... columns) {
        return selectAll(Arrays.stream(columns)
           .map(v -> asName(String.valueOf(v)))
           .toArray(Expression[]::new));
    }

    /**
     * SELECT ALL statement.
     * Example:
     * <code>
     * select(asName("id"), asName("name")); // SELECT id, name
     * </code>
     *
     * @param columns output columns
     * @return select from step
     */
    public final SelectFromStep selectAll(Expression... columns) {
        return initSelect(columns).all();
    }

    /**
     * SELECT DISTINCT statement.
     * Example:
     * <code>
     * selectDistinct("id", "name"); // SELECT DISTINCT id, name
     * </code>
     *
     * @param columns output columns
     * @return select from step
     */
    @SafeVarargs
    public final <T> SelectFromStep selectDistinct(T... columns) {
        return selectDistinct(Arrays.stream(columns)
           .map(v -> asName(String.valueOf(v)))
           .toArray(Expression[]::new));
    }

    /**
     * SELECT DISTINCT statement.
     * Example:
     * <code>
     * selectDistinct(asName("id"), asName("name")); // SELECT DISTINCT id, name
     * </code>
     *
     * @param columns output columns
     * @return select from step
     */
    public final SelectFromStep selectDistinct(Expression... columns) {
        return initSelect(columns).distinct();
    }

    /**
     * SELECT DISTINCT ON (..) .. statement.
     * Example:
     * <code>
     * String[] distinct = {"id"};
     * selectDistinctOn(distinct, "id", "name"); // SELECT DISTINCT ON (id) id, name
     * </code>
     *
     * @param columns output columns
     * @return select from step
     */
    @SafeVarargs
    public final <T> SelectFromStep selectDistinctOn(String[] distinct, T... columns) {
        return selectDistinctOn(
           Arrays.stream(distinct).map(Queryman::asName).toArray(Expression[]::new),
           Arrays.stream(columns)
              .map(v -> asName(String.valueOf(v)))
              .toArray(Expression[]::new)
        );
    }

    /**
     * SELECT DISTINCT ON (..) .. statement.
     * Example:
     * <code>
     * Expression[] distinct = {"id"};
     * selectDistinctOn(distinct, asName("id"), asName("name")); // SELECT DISTINCT ON (id) id, name
     * </code>
     *
     * @param columns output columns
     * @return select from step
     */
    public final SelectFromStep selectDistinctOn(Expression[] distinct, Expression... columns) {
        return initSelect(columns).distinctOn(distinct);
    }

    private class WithQuery {
        private final Expression   name;
        private final Expression[] columns;

        private Expression query;

        public WithQuery(Expression name, Expression[] columns) {
            this.name = name;
            this.columns = columns;
        }

        public void setQuery(Expression query) {
            this.query = query;
        }
    }
}
