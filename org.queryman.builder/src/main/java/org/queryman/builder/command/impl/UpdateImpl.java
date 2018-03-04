/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.AbstractQuery;
import org.queryman.builder.PostgreSQL;
import org.queryman.builder.Query;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.command.update.UpdateAsStep;
import org.queryman.builder.command.update.UpdateFromStep;
import org.queryman.builder.command.update.UpdateReturningStep;
import org.queryman.builder.command.update.UpdateSetStep;
import org.queryman.builder.command.update.UpdateWhereFirstStep;
import org.queryman.builder.command.update.UpdateWhereManyStep;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.queryman.builder.Keywords.SET;
import static org.queryman.builder.Keywords.UPDATE;
import static org.queryman.builder.Keywords.UPDATE_ONLY;
import static org.queryman.builder.Operators.EQUAL;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asSubQuery;
import static org.queryman.builder.PostgreSQL.condition;
import static org.queryman.builder.PostgreSQL.conditionExists;
import static org.queryman.builder.PostgreSQL.nodeMetadata;
import static org.queryman.builder.ast.NodesMetadata.AS;
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
   UpdateFromStep,
   UpdateWhereFirstStep,
   UpdateWhereManyStep,
   UpdateReturningStep {

    private final Expression   table;
    private final boolean      only;
    private final List<Map<Expression, Expression>> setList = new ArrayList<>();
    private       String       alias;
    private       Expression[] fromList;
    private       Expression[] returning;
    private       Conditions   conditions;
    private       String       whereCurrentOf;

    public UpdateImpl(AbstractSyntaxTree tree, Expression table) {
        this(tree, table, false);
    }

    public UpdateImpl(AbstractSyntaxTree tree, Expression table, boolean only) {
        super(tree);
        this.table = table;
        this.only = only;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
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
    }

    @Override
    public final UpdateImpl as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public final UpdateImpl where(String left, String operator, String right) {
        return where(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl where(Expression left, Operator operator, Expression right) {
        return where(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl where(Expression field, Operator operator, Query query) {
        return where(condition(field, operator, query));
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
    public final UpdateImpl and(String left, String operator, String right) {
        return and(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl and(Expression left, Operator operator, Expression right) {
        return and(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl and(Expression field, Operator operator, Query query) {
        return and(condition(field, operator, query));
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
    public final UpdateImpl andNot(String left, String operator, String right) {
        return andNot(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl andNot(Expression left, Operator operator, Expression right) {
        return andNot(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl andNot(Expression field, Operator operator, Query query) {
        return andNot(condition(field, operator, query));
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
    public final UpdateImpl or(String left, String operator, String right) {
        return or(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl or(Expression left, Operator operator, Expression right) {
        return or(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl or(Expression field, Operator operator, Query query) {
        return or(condition(field, operator, query));
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
    public final UpdateImpl orNot(String left, String operator, String right) {
        return orNot(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl orNot(Expression left, Operator operator, Expression right) {
        return orNot(condition(left, operator, right));
    }

    @Override
    public final UpdateImpl orNot(Expression field, Operator operator, Query query) {
        return orNot(condition(field, operator, query));
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
    public final UpdateImpl returning(String... output) {
        return returning(Arrays.stream(output).map(PostgreSQL::asName).toArray(Expression[]::new));
    }

    @Override
    public final UpdateImpl returning(Expression... output) {
        returning = output;
        return this;
    }

    @Override
    public final <T> UpdateImpl set(String column, T value) {
        return set(asName(column), asName(String.valueOf(value)));
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
        return from(Arrays.stream(tables).map(PostgreSQL::asName).toArray(Expression[]::new));
    }

    @Override
    public final UpdateImpl from(Expression... tables) {
        fromList = tables;
        return this;
    }
}
