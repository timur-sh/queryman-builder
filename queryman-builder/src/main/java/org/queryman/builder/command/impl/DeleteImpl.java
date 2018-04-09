/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.AbstractQuery;
import org.queryman.builder.Query;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.command.delete.DeleteAsStep;
import org.queryman.builder.command.delete.DeleteFinalStep;
import org.queryman.builder.command.delete.DeleteReturningStep;
import org.queryman.builder.command.delete.DeleteUsingStep;
import org.queryman.builder.command.delete.DeleteWhereFirstStep;
import org.queryman.builder.command.delete.DeleteWhereManySteps;
import org.queryman.builder.token.Expression;
import org.queryman.builder.utils.ArrayUtils;

import static org.queryman.builder.Keywords.DELETE_FROM;
import static org.queryman.builder.Keywords.DELETE_FROM_ONLY;
import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.Queryman.condition;
import static org.queryman.builder.Queryman.conditionExists;
import static org.queryman.builder.Queryman.nodeMetadata;
import static org.queryman.builder.ast.NodesMetadata.AS;
import static org.queryman.builder.ast.NodesMetadata.EMPTY;
import static org.queryman.builder.ast.NodesMetadata.RETURNING;
import static org.queryman.builder.ast.NodesMetadata.USING;
import static org.queryman.builder.ast.NodesMetadata.WHERE;
import static org.queryman.builder.ast.NodesMetadata.WHERE_CURRENT_OF;

/**
 * DELETE statement.
 *
 * @author Timur Shaidullin
 */
public class DeleteImpl extends AbstractQuery implements
   DeleteAsStep,
   DeleteUsingStep,
   DeleteWhereFirstStep,
   DeleteWhereManySteps,
   DeleteReturningStep,
   DeleteFinalStep {

    private final Expression   table;
    private final boolean      only;
    private       String       alias;
    private       Expression[] usingList;
    private       Expression[] returning;
    private       Conditions   conditions;
    private       String       whereCurrentOf;

    private WithImpl with;

    public DeleteImpl(Expression table) {
        this(table, false);
    }

    public DeleteImpl(Expression table, boolean only) {
        this.table = table;
        this.only = only;
    }

    void setWith(WithImpl with) {
        this.with = with;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        if (with != null)
            tree.startNode(EMPTY)
               .peek(with);

        if (only)
            tree.startNode(nodeMetadata(DELETE_FROM_ONLY));
        else
            tree.startNode(nodeMetadata(DELETE_FROM));

        tree.addLeaf(table);

        if (alias != null)
            tree.startNode(AS)
               .addLeaf(asName(alias))
               .endNode();

        if (usingList != null)
            tree.startNode(USING, ", ")
               .addLeaves(usingList)
               .endNode();

        if (conditions != null) {
            tree.startNode(WHERE);
            tree.peek(conditions);
            tree.endNode();
        } else if (whereCurrentOf != null)
            tree.startNode(WHERE_CURRENT_OF, ", ")
               .addLeaf(asName(whereCurrentOf))
               .endNode();

        if (returning != null)
            tree.startNode(RETURNING, ", ")
               .addLeaves(returning)
               .endNode();

        tree.endNode();

        if (with != null)
            tree.endNode();
    }

    @Override
    public final DeleteImpl as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public final DeleteImpl using(Expression... tables) {
        usingList = tables;
        return this;
    }

    @Override
    public final DeleteImpl using(String... tables) {
        return using(ArrayUtils.toExpressions(tables));
    }

    @Override
    public final <T> DeleteImpl where(T left, T operator, T right) {
        return where(condition(left, operator, right));
    }

    @Override
    public final DeleteImpl where(Conditions conditions) {
        this.conditions = new ConditionsImpl(conditions);
        return this;
    }

    @Override
    public final DeleteImpl whereExists(Query query) {
        return where(conditionExists(query));
    }

    @Override
    public final DeleteImpl whereCurrentOf(String cursorName) {
        this.whereCurrentOf = cursorName;
        return this;
    }

    @Override
    public final <T> DeleteImpl and(T left, T operator, T right) {
        and(condition(left, operator, right));
        return this;
    }

    @Override
    public final DeleteImpl and(Conditions conditions) {
        this.conditions.and(conditions);
        return this;
    }

    @Override
    public final DeleteImpl andExists(Query query) {
        return and(conditionExists(query));
    }

    @Override
    public final <T> DeleteImpl andNot(T left, T operator, T right) {
        andNot(condition(left, operator, right));
        return this;
    }

    @Override
    public final DeleteImpl andNot(Conditions conditions) {
        this.conditions.andNot(conditions);
        return this;
    }

    @Override
    public final DeleteImpl andNotExists(Query query) {
        return andNot(conditionExists(query));
    }

    @Override
    public final <T> DeleteImpl or(T left, T operator, T right) {
        or(condition(left, operator, right));
        return this;
    }

    @Override
    public final DeleteImpl or(Conditions conditions) {
        this.conditions.or(conditions);
        return this;
    }

    @Override
    public final DeleteImpl orExists(Query query) {
        return or(conditionExists(query));
    }

    @Override
    public final <T> DeleteImpl orNot(T left, T operator, T right) {
        orNot(condition(left, operator, right));
        return this;
    }

    @Override
    public final DeleteImpl orNot(Conditions conditions) {
        this.conditions.orNot(conditions);
        return this;
    }

    @Override
    public final DeleteImpl orNotExists(Query query) {
        return orNot(conditionExists(query));
    }

    @Override
    @SafeVarargs
    public final <T> DeleteImpl returning(T... output) {
        return returning(ArrayUtils.toExpressions(output));
    }

    @Override
    public final DeleteImpl returning(Expression... output) {
        returning = output;
        return this;
    }
}
