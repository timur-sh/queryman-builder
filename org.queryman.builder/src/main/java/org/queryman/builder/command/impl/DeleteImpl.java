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
import org.queryman.builder.command.delete.DeleteWhereManyStep;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

import static org.queryman.builder.Keywords.DELETE_FROM;
import static org.queryman.builder.Keywords.DELETE_FROM_ONLY;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.condition;
import static org.queryman.builder.PostgreSQL.conditionExists;
import static org.queryman.builder.PostgreSQL.nodeMetadata;
import static org.queryman.builder.ast.NodesMetadata.AS;
import static org.queryman.builder.ast.NodesMetadata.RETURNING;
import static org.queryman.builder.ast.NodesMetadata.USING;
import static org.queryman.builder.ast.NodesMetadata.WHERE;
import static org.queryman.builder.ast.NodesMetadata.WHERE_CURRENT_OF;
import static org.queryman.builder.utils.ArrayUtils.*;

/**
 * DELETE statement.
 *
 * @author Timur Shaidullin
 */
public class DeleteImpl extends AbstractQuery implements
   DeleteAsStep,
   DeleteUsingStep,
   DeleteWhereFirstStep,
   DeleteWhereManyStep,
   DeleteReturningStep,
   DeleteFinalStep {

    private final Expression   table;
    private final boolean      only;
    private       String       alias;
    private       Expression[] usingList;
    private       Expression[] returning;
    private       Conditions   conditions;
    private       String       whereCurrentOf;

    public DeleteImpl(AbstractSyntaxTree tree, Expression table) {
        this(tree, table, false);
    }

    public DeleteImpl(AbstractSyntaxTree tree, Expression table, boolean only) {
        super(tree);
        this.table = table;
        this.only = only;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
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
        return using(toExpression(tables));
    }

    @Override
    public final DeleteImpl where(String left, String operator, String right) {
        return where(condition(left, operator, right));
    }

    @Override
    public final DeleteImpl where(Expression left, Operator operator, Expression right) {
        return where(condition(left, operator, right));
    }

    @Override
    public final DeleteImpl where(Expression field, Operator operator, Query query) {
        return where(condition(field, operator, query));
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
    public final DeleteImpl and(String left, String operator, String right) {
        and(condition(left, operator, right));
        return this;
    }

    @Override
    public final DeleteImpl and(Expression left, Operator operator, Expression right) {
        and(condition(left, operator, right));
        return this;
    }

    @Override
    public final DeleteImpl and(Expression field, Operator operator, Query query) {
        and(condition(field, operator, query));
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
    public final DeleteImpl andNot(String left, String operator, String right) {
        andNot(condition(left, operator, right));
        return this;
    }

    @Override
    public final DeleteImpl andNot(Expression left, Operator operator, Expression right) {
        andNot(condition(left, operator, right));
        return this;
    }

    @Override
    public final DeleteImpl andNot(Expression field, Operator operator, Query query) {
        andNot(condition(field, operator, query));
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
    public final DeleteImpl or(String left, String operator, String right) {
        or(condition(left, operator, right));
        return this;
    }

    @Override
    public final DeleteImpl or(Expression left, Operator operator, Expression right) {
        or(condition(left, operator, right));
        return this;
    }

    @Override
    public final DeleteImpl or(Expression field, Operator operator, Query query) {
        or(condition(field, operator, query));
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
    public final DeleteImpl orNot(String left, String operator, String right) {
        orNot(condition(left, operator, right));
        return this;
    }

    @Override
    public final DeleteImpl orNot(Expression left, Operator operator, Expression right) {
        orNot(condition(left, operator, right));
        return this;
    }

    @Override
    public final DeleteImpl orNot(Expression field, Operator operator, Query query) {
        orNot(condition(field, operator, query));
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
        return returning(toExpression(output));
    }

    @Override
    public final DeleteImpl returning(Expression... output) {
        returning = output;
        return this;
    }
}
