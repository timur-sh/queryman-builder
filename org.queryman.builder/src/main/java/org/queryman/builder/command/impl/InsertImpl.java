/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.AbstractQuery;
import org.queryman.builder.command.ConflictTarget;
import org.queryman.builder.Query;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.command.insert.InsertAsStep;
import org.queryman.builder.command.insert.InsertColumnsManyStep;
import org.queryman.builder.command.insert.InsertColumnsStep;
import org.queryman.builder.command.insert.InsertConflictActionStep;
import org.queryman.builder.command.insert.InsertDefaultValuesStep;
import org.queryman.builder.command.insert.InsertDoUpdateSetStep;
import org.queryman.builder.command.insert.InsertDoUpdateWhereFirstStep;
import org.queryman.builder.command.insert.InsertDoUpdateWhereManyStep;
import org.queryman.builder.command.insert.InsertOnConflictStep;
import org.queryman.builder.command.insert.InsertOnConflictWhereFirstStep;
import org.queryman.builder.command.insert.InsertOnConflictWhereManyStep;
import org.queryman.builder.command.insert.InsertOnConstraintStep;
import org.queryman.builder.command.insert.InsertOverridingStep;
import org.queryman.builder.command.insert.InsertValuesStep;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

/**
 * @author Timur Shaidullin
 */
public class InsertImpl extends AbstractQuery implements
   InsertAsStep,
   InsertColumnsStep,
   InsertColumnsManyStep,
   InsertOverridingStep,
   InsertDefaultValuesStep,
   InsertValuesStep,
   InsertOnConflictStep,
   InsertOnConflictWhereFirstStep,
   InsertOnConflictWhereManyStep,
   InsertOnConstraintStep,
   InsertConflictActionStep,
   InsertDoUpdateSetStep,
   InsertDoUpdateWhereFirstStep,
   InsertDoUpdateWhereManyStep
{
    private final Expression table;

    public InsertImpl(AbstractSyntaxTree tree, Expression table) {
        super(tree);
        this.table = table;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {

    }

    @Override
    public final InsertImpl as(String alias) {
        return this;
    }

    @Override
    public final InsertImpl columns(String... columns) {
        return this;
    }

    @Override
    public final InsertImpl columns(Expression... columns) {
        return this;
    }

    @Override
    public final InsertImpl doNothing() {
        return this;
    }

    @Override
    public final InsertImpl doUpdate() {
        return this;
    }

    @Override
    public final InsertImpl where(String left, String operator, String right) {
        return this;
    }

    @Override
    public final InsertImpl where(Expression left, Operator operator, Expression right) {
        return this;
    }

    @Override
    public final InsertImpl where(Expression field, Operator operator, Query query) {
        return this;
    }

    @Override
    public final InsertImpl where(Conditions conditions) {
        return this;
    }

    @Override
    public final InsertImpl whereExists(Query query) {
        return this;
    }

    @Override
    public final InsertImpl and(String left, String operator, String right) {
        return this;
    }

    @Override
    public final InsertImpl and(Expression left, Operator operator, Expression right) {
        return this;
    }

    @Override
    public final InsertImpl and(Expression field, Operator operator, Query query) {
        return this;
    }

    @Override
    public final InsertImpl and(Conditions conditions) {
        return this;
    }

    @Override
    public final InsertImpl andExists(Query query) {
        return this;
    }

    @Override
    public final InsertImpl andNot(String left, String operator, String right) {
        return this;
    }

    @Override
    public final InsertImpl andNot(Expression left, Operator operator, Expression right) {
        return this;
    }

    @Override
    public final InsertImpl andNot(Expression field, Operator operator, Query query) {
        return this;
    }

    @Override
    public final InsertImpl andNot(Conditions conditions) {
        return this;
    }

    @Override
    public final InsertImpl andNotExists(Query query) {
        return this;
    }

    @Override
    public final InsertImpl or(String left, String operator, String right) {
        return this;
    }

    @Override
    public final InsertImpl or(Expression left, Operator operator, Expression right) {
        return this;
    }

    @Override
    public final InsertImpl or(Expression field, Operator operator, Query query) {
        return this;
    }

    @Override
    public final InsertImpl or(Conditions conditions) {
        return this;
    }

    @Override
    public final InsertImpl orExists(Query query) {
        return this;
    }

    @Override
    public final InsertImpl orNot(String left, String operator, String right) {
        return this;
    }

    @Override
    public final InsertImpl orNot(Expression left, Operator operator, Expression right) {
        return this;
    }

    @Override
    public final InsertImpl orNot(Expression field, Operator operator, Query query) {
        return this;
    }

    @Override
    public final InsertImpl orNot(Conditions conditions) {
        return this;
    }

    @Override
    public final InsertImpl orNotExists(Query query) {
        return this;
    }

    @Override
    public final InsertImpl defaultValues() {
        return this;
    }

    @Override
    public final <T> InsertImpl set(String column, T value) {
        return this;
    }

    @Override
    public final InsertImpl set(Expression listColumns, Expression listValues) {
        return this;
    }

    @Override
    public final InsertImpl set(Expression listColumns, Query subSelect) {
        return this;
    }

    @Override
    public final InsertImpl onConstraint(String name) {
        return this;
    }

    @Override
    public final InsertImpl overridingSystemValue() {
        return this;
    }

    @Override
    public final InsertImpl overridingUserValue() {
        return this;
    }

    @Override
    @SafeVarargs
    public final <T> InsertImpl values(T... values) {
        return this;
    }

    @Override
    public final InsertImpl values(Expression... values) {
        return this;
    }

    @Override
    public final InsertImpl values(Query query) {
        return this;
    }

    @Override
    public final InsertImpl onConflict() {
        return this;
    }

    @Override
    public final InsertImpl onConflict(ConflictTarget... targets) {
        return this;
    }

    @Override
    public final InsertImpl returning(String... output) {
        return this;
    }

    @Override
    public final InsertImpl returning(Expression... output) {
        return this;
    }
}
