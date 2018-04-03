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
import org.queryman.builder.command.update.UpdateAsStep;
import org.queryman.builder.command.update.UpdateFromStep;
import org.queryman.builder.command.update.UpdateReturningStep;
import org.queryman.builder.command.update.UpdateSetManyStep;
import org.queryman.builder.command.update.UpdateSetStep;
import org.queryman.builder.command.update.UpdateWhereFirstStep;
import org.queryman.builder.command.update.UpdateWhereManySteps;
import org.queryman.builder.token.Expression;
import org.queryman.builder.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.queryman.builder.Keywords.SET;
import static org.queryman.builder.Keywords.UPDATE;
import static org.queryman.builder.Keywords.UPDATE_ONLY;
import static org.queryman.builder.Operators.EQUAL;
import static org.queryman.builder.Queryman.asConstant;
import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.Queryman.asSubQuery;
import static org.queryman.builder.Queryman.condition;
import static org.queryman.builder.Queryman.conditionExists;
import static org.queryman.builder.Queryman.nodeMetadata;
import static org.queryman.builder.ast.NodesMetadata.AS;
import static org.queryman.builder.ast.NodesMetadata.EMPTY;
import static org.queryman.builder.ast.NodesMetadata.FROM;
import static org.queryman.builder.ast.NodesMetadata.RETURNING;
import static org.queryman.builder.ast.NodesMetadata.WHERE;
import static org.queryman.builder.ast.NodesMetadata.WHERE_CURRENT_OF;

/**
 * UPDATE statement.
 *
 * @author Timur Shaidullin
 */
public class UpdateImpl extends AbstractQuery implements
   UpdateAsStep,
   UpdateSetStep,
   UpdateSetManyStep,
   UpdateFromStep,
   UpdateWhereFirstStep,
   UpdateWhereManySteps,
   UpdateReturningStep {

    private final Expression   table;
    private final boolean      only;
    private final List<Map<Expression, Expression>> setList = new ArrayList<>();
    private       String       alias;
    private       Expression[] fromList;
    private       Expression[] returning;
    private       Conditions   conditions;
    private       String       whereCurrentOf;

    private WithImpl with;

    public UpdateImpl(Expression table) {
        this(table, false);
    }

    public UpdateImpl(Expression table, boolean only) {
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
            tree.startNode(nodeMetadata(UPDATE_ONLY));
        else
            tree.startNode(nodeMetadata(UPDATE));

        tree.addLeaf(table);

        if (alias != null)
            tree.startNode(AS)
               .addLeaf(asName(alias))
               .endNode();

        if (setList.size() > 0) {
            tree.startNode(nodeMetadata(SET).setJoinNodes(true), ", ");

            for (Map<Expression, Expression> map : setList)
                for (Expression key : map.keySet())
                    tree.startNode(nodeMetadata(EQUAL))
                       .addLeaves(key, map.get(key))
                       .endNode();

            tree.endNode();
        }

        if (fromList != null)
            tree.startNode(FROM, ", ")
               .addLeaves(fromList)
               .endNode();

        if (conditions != null) {
            tree.startNode(WHERE)
               .peek(conditions)
               .endNode();

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
    public final UpdateImpl as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public final <T> UpdateImpl where(T left, T operator, T right) {
        return where(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl where(Conditions conditions) {
        this.conditions = new ConditionsImpl(conditions);
        return this;
    }

    @Override
    public final UpdateImpl whereExists(Query query) {
        return where(conditionExists(query));
    }

    @Override
    public final UpdateImpl whereCurrentOf(String cursorName) {
        this.whereCurrentOf = cursorName;
        return this;
    }

    @Override
    public final <T> UpdateImpl and(T left, T operator, T right) {
        return and(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl and(Conditions conditions) {
        this.conditions.and(conditions);
        return this;
    }

    @Override
    public final UpdateImpl andExists(Query query) {
        return and(conditionExists(query));
    }

    @Override
    public final <T> UpdateImpl andNot(T left, T operator, T right) {
        return andNot(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl andNot(Conditions conditions) {
        this.conditions.andNot(conditions);
        return this;
    }

    @Override
    public final UpdateImpl andNotExists(Query query) {
        return andNot(conditionExists(query));
    }

    @Override
    public final <T> UpdateImpl or(T left, T operator, T right) {
        return or(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl or(Conditions conditions) {
        this.conditions.or(conditions);
        return this;
    }

    @Override
    public final UpdateImpl orExists(Query query) {
        return or(conditionExists(query));
    }

    @Override
    public final <T> UpdateImpl orNot(T left, T operator, T right) {
        return orNot(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl orNot(Conditions conditions) {
        this.conditions.orNot(conditions);
        return this;
    }

    @Override
    public final UpdateImpl orNotExists(Query query) {
        return orNot(conditionExists(query));
    }

    @Override
    @SafeVarargs
    public final <T> UpdateImpl returning(T... output) {
        return returning(ArrayUtils.toExpressions(output));
    }

    @Override
    public final UpdateImpl returning(Expression... output) {
        returning = output;
        return this;
    }

    @Override
    public final <T> UpdateImpl set(String column, T value) {
        return set(asName(column), asConstant(value));
    }

    @Override
    public final UpdateImpl set(Expression listColumns, Expression listValues) {
        setList.add(Map.of(listColumns, listValues));
        return this;
    }

    @Override
    public final UpdateImpl set(Expression listColumns, Query subSelect) {
        return set(listColumns, asSubQuery(subSelect));
    }

    @Override
    public final UpdateImpl from(String... tables) {
        return from(ArrayUtils.toExpressions(tables));
    }

    @Override
    public final UpdateImpl from(Expression... tables) {
        fromList = tables;
        return this;
    }
}
