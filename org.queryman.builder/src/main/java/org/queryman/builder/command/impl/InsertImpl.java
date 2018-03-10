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
import org.queryman.builder.ast.NodesMetadata;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.command.ConflictTarget;
import org.queryman.builder.command.insert.InsertAsStep;
import org.queryman.builder.command.insert.InsertColumnsManyStep;
import org.queryman.builder.command.insert.InsertColumnsStep;
import org.queryman.builder.command.insert.InsertConflictActionStep;
import org.queryman.builder.command.insert.InsertDefaultValuesStep;
import org.queryman.builder.command.insert.InsertDoUpdateSetStep;
import org.queryman.builder.command.insert.InsertDoUpdateWhereFirstStep;
import org.queryman.builder.command.insert.InsertDoUpdateWhereManySteps;
import org.queryman.builder.command.insert.InsertOnConflictStep;
import org.queryman.builder.command.insert.InsertOnConflictWhereFirstStep;
import org.queryman.builder.command.insert.InsertOnConflictWhereManySteps;
import org.queryman.builder.command.insert.InsertOnConstraintStep;
import org.queryman.builder.command.insert.InsertOverridingStep;
import org.queryman.builder.command.insert.InsertValuesManyStep;
import org.queryman.builder.command.insert.InsertValuesStep;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.queryman.builder.Keywords.AS;
import static org.queryman.builder.Keywords.DEFAULT_VALUES;
import static org.queryman.builder.Keywords.DO_NOTHING;
import static org.queryman.builder.Keywords.DO_UPDATE;
import static org.queryman.builder.Keywords.INSERT_INTO;
import static org.queryman.builder.Keywords.ON_CONFLICT;
import static org.queryman.builder.Keywords.ON_CONSTRAINT;
import static org.queryman.builder.Keywords.OVERRIDING_SYSTEM_VALUE;
import static org.queryman.builder.Keywords.OVERRIDING_USER_VALUE;
import static org.queryman.builder.Keywords.SET;
import static org.queryman.builder.Keywords.VALUES;
import static org.queryman.builder.Operators.EQUAL;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asSubQuery;
import static org.queryman.builder.PostgreSQL.condition;
import static org.queryman.builder.PostgreSQL.conditionExists;
import static org.queryman.builder.PostgreSQL.nodeMetadata;
import static org.queryman.builder.ast.NodesMetadata.EMPTY_GROUPED;
import static org.queryman.builder.ast.NodesMetadata.RETURNING;
import static org.queryman.builder.utils.ArrayUtils.toExpression;

/**
 * @author Timur Shaidullin
 */
public class InsertImpl extends AbstractQuery implements
   InsertAsStep,
   InsertColumnsStep,
   InsertColumnsManyStep,
   InsertOverridingStep,
   InsertDefaultValuesStep,
   InsertValuesManyStep,
   InsertValuesStep,
   InsertOnConflictStep,
   InsertOnConflictWhereFirstStep,
   InsertOnConflictWhereManySteps,
   InsertOnConstraintStep,
   InsertConflictActionStep,
   InsertDoUpdateSetStep,
   InsertDoUpdateWhereFirstStep,
   InsertDoUpdateWhereManySteps {

    private final Expression table;
    private final List<Map<Expression, Expression>> setList = new ArrayList<>();
    private Expression       alias;
    private Expression[]     columns;
    private boolean          overridingSystemValue;
    private boolean          overridingUserValue;
    private boolean          defaultValues;
    private Expression[]     values;
    private Query            queryValues;
    private boolean          onConflict;
    private ConflictTarget[] conflictTargets;
    private Expression       onConstraint;
    private boolean          doNothing;
    private boolean          doUpdate;
    private Conditions       onConflictConditions;
    private Conditions       conflictActionCondition;
    private boolean onConflictProcessing     = true;
    private boolean conflictActionProcessing = false;
    private Expression[] returning;

    public InsertImpl(AbstractSyntaxTree tree, Expression table) {
        super(tree);
        this.table = table;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        tree.startNode(nodeMetadata(INSERT_INTO));
        tree.addLeaf(table);

        if (alias != null)
            tree.startNode(nodeMetadata(AS))
               .addLeaf(alias)
               .endNode();

        if (columns != null)
            tree.startNode(EMPTY_GROUPED, ", ")
               .addLeaves(columns)
               .endNode();

        if (overridingSystemValue)
            tree.startNode(nodeMetadata(OVERRIDING_SYSTEM_VALUE)).endNode();
        else if (overridingUserValue)
            tree.startNode(nodeMetadata(OVERRIDING_USER_VALUE)).endNode();

        if (defaultValues)
            tree.startNode(nodeMetadata(DEFAULT_VALUES)).endNode();
        else if (values != null)
            tree.startNode(nodeMetadata(VALUES), ", ")
               .startNode(EMPTY_GROUPED, ", ")
               .addLeaves(values)
               .endNode()
               .endNode();
        else if (queryValues != null)
            tree.startNode(nodeMetadata(VALUES), ", ")
               .addLeaf(asSubQuery(queryValues))
               .endNode();

        if (onConflict) {
            tree.startNode(nodeMetadata(ON_CONFLICT));
            if (conflictTargets != null) {
                tree.startNode(EMPTY_GROUPED.setJoinNodes(true), ", ");
                for (ConflictTarget target : conflictTargets)
                    tree.peek(target);
                tree.endNode();

                if (onConflictConditions != null)
                    tree.startNode(NodesMetadata.WHERE)
                       .peek(onConflictConditions)
                       .endNode();
            } else if (onConstraint != null)
                tree.startNode(nodeMetadata(ON_CONSTRAINT))
                   .addLeaf(onConstraint)
                   .endNode();

            tree.endNode();

            if (doNothing)
                tree.startNode(nodeMetadata(DO_NOTHING)).endNode();
            else if (doUpdate) {
                tree.startNode(nodeMetadata(DO_UPDATE));

                if (setList.size() > 0) {
                    tree.startNode(nodeMetadata(SET).setJoinNodes(true), ", ");

                    for (Map<Expression, Expression> map : setList)
                        for (Expression key : map.keySet())
                            tree.startNode(nodeMetadata(EQUAL))
                               .addLeaves(key, map.get(key))
                               .endNode();

                    tree.endNode();
                }

                if (conflictActionCondition != null)
                    tree.startNode(NodesMetadata.WHERE)
                       .peek(conflictActionCondition)
                       .endNode();

                tree.endNode();
            } else
                throw new IllegalStateException("Neither DO NOTHING or DO UPDATE is specified");
        }

        if (returning != null)
            tree.startNode(RETURNING, ", ")
               .addLeaves(returning)
               .endNode();

        tree.endNode();
    }

    @Override
    public final InsertImpl as(String alias) {
        this.alias = asName(alias);
        return this;
    }

    @Override
    public final InsertImpl columns(String... columns) {
        return columns(toExpression(columns));
    }

    @Override
    public final InsertImpl columns(Expression... columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public final InsertImpl overridingSystemValue() {
        overridingSystemValue = true;
        return this;
    }

    @Override
    public final InsertImpl overridingUserValue() {
        overridingUserValue = true;
        return this;
    }

    @Override
    public final InsertImpl defaultValues() {
        defaultValues = true;
        return this;
    }

    @Override
    @SafeVarargs
    public final <T> InsertImpl values(T... values) {
        return values(toExpression(PostgreSQL::asConstant, values));
    }

    @Override
    public final InsertImpl values(Expression... values) {
        Function<Expression, Expression> func = v -> v == null ? asConstant(null) :v;
        this.values = toExpression(func, values);
        return this;
    }

    @Override
    public final InsertImpl values(Query query) {
        queryValues = query;
        return this;
    }

    @Override
    public final InsertImpl onConflict() {
        onConflict = true;
        return this;
    }

    @Override
    public final InsertImpl onConflict(ConflictTarget... targets) {
        onConflict();
        onConflictProcessing = true;
        conflictActionProcessing = false;

        conflictTargets = targets;
        return this;
    }

    @Override
    public final InsertImpl onConstraint(String name) {
        onConstraint = asName(name);
        return this;
    }

    @Override
    public final InsertImpl doNothing() {
        doNothing = true;
        return this;
    }

    @Override
    public final InsertImpl doUpdate() {
        doUpdate = true;

        onConflictProcessing = false;
        conflictActionProcessing = true;
        return this;
    }

    @Override
    public final <T> InsertImpl where(T left, T operator, T right) {
        where(condition(left, operator, right));

        return this;
    }

    @Override
    public final InsertImpl where(Conditions conditions) {
        if (onConflictProcessing)
            this.onConflictConditions = new ConditionsImpl(conditions);
        else if (conflictActionProcessing)
            this.conflictActionCondition = new ConditionsImpl(conditions);
        else
            throw new IllegalArgumentException("Unknown conditions");

        return this;
    }

    @Override
    public final InsertImpl whereExists(Query query) {
        where(conditionExists(query));

        return this;
    }

    @Override
    public final <T> InsertImpl and(T left, T operator, T right) {
        and(condition(left, operator, right));

        return this;
    }

    @Override
    public final InsertImpl and(Conditions conditions) {
        if (onConflictProcessing)
            onConflictConditions.and(conditions);
        else if (conflictActionProcessing)
            conflictActionCondition.and(conditions);
        else
            throw new IllegalArgumentException("Unknown conditions");

        return this;
    }

    @Override
    public final InsertImpl andExists(Query query) {
        and(conditionExists(query));
        return this;
    }

    @Override
    public final <T> InsertImpl andNot(T left, T operator, T right) {
        andNot(condition(left, operator, right));

        return this;
    }

    @Override
    public final InsertImpl andNot(Conditions conditions) {
        if (onConflictProcessing)
            onConflictConditions.andNot(conditions);
        else if (conflictActionProcessing)
            conflictActionCondition.andNot(conditions);
        else
            throw new IllegalArgumentException("Unknown conditions");

        return this;
    }

    @Override
    public final InsertImpl andNotExists(Query query) {
        andNot(conditionExists(query));
        return this;
    }

    @Override
    public final <T> InsertImpl or(T left, T operator, T right) {
        or(condition(left, operator, right));

        return this;
    }

    @Override
    public final InsertImpl or(Conditions conditions) {
        if (onConflictProcessing)
            onConflictConditions.or(conditions);
        else if (conflictActionProcessing)
            conflictActionCondition.or(conditions);
        else
            throw new IllegalArgumentException("Unknown conditions");

        return this;
    }

    @Override
    public final InsertImpl orExists(Query query) {
        or(conditionExists(query));
        return this;
    }

    @Override
    public final <T> InsertImpl orNot(T left, T operator, T right) {
        orNot(condition(left, operator, right));

        return this;
    }

    @Override
    public final InsertImpl orNot(Conditions conditions) {
        if (onConflictProcessing)
            onConflictConditions.orNot(conditions);
        else if (conflictActionProcessing)
            conflictActionCondition.orNot(conditions);
        else
            throw new IllegalArgumentException("Unknown conditions");

        return this;
    }

    @Override
    public final InsertImpl orNotExists(Query query) {
        orNot(conditionExists(query));
        return this;
    }

    @Override
    public final <T> InsertImpl set(String column, T value) {
        return set(asName(column), asConstant(value));
    }

    @Override
    public final InsertImpl set(Expression listColumns, Expression listValues) {
        setList.add(Map.of(listColumns, listValues));
        return this;
    }

    @Override
    public final InsertImpl set(Expression listColumns, Query subSelect) {
        return set(listColumns, asSubQuery(subSelect));
    }

    @Override
    @SafeVarargs
    public final <T> InsertImpl returning(T... output) {
        return returning(toExpression(output));
    }

    @Override
    public final InsertImpl returning(Expression... output) {
        returning = output;
        return this;
    }
}
