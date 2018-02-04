/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.Operators;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.NodeMetadata;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;
import org.queryman.builder.token.Token;

import java.util.LinkedList;
import java.util.List;

import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.condition;
import static org.queryman.builder.PostgreSQL.operator;
import static org.queryman.builder.ast.NodesMetadata.AND;
import static org.queryman.builder.ast.NodesMetadata.AND_NOT;
import static org.queryman.builder.ast.NodesMetadata.EMPTY;
import static org.queryman.builder.ast.NodesMetadata.EMPTY_GROUPED;
import static org.queryman.builder.ast.NodesMetadata.OR;
import static org.queryman.builder.ast.NodesMetadata.OR_NOT;
import static org.queryman.builder.utils.StringUtils.isEmpty;

/**
 * @author Timur Shaidullin
 */
public final class ConditionsImpl implements
   Conditions {

    private NodeMetadata metadata;
    private Token        leftValue;
    private Operator     operator;
    private Token        rightValue;

    private final List<Conditions> CONDITIONS = new LinkedList<>();

    public ConditionsImpl(String leftValue, String operator, String rightValue) {
        this(asConstant(leftValue), Operators.map(operator), asConstant(rightValue));
    }

    public ConditionsImpl(Expression leftValue, Operator operator, Expression rightValue) {
        this.leftValue = leftValue;
        this.operator = operator;
        this.rightValue = rightValue;
    }

    ConditionsImpl(NodeMetadata metadata, Conditions conditions) {
        CONDITIONS.add(conditions);
        this.metadata = metadata;
    }

    @Override
    public final Conditions and(String leftValue, String operator, String rightValue) {
        and(asConstant(leftValue), operator, asConstant(rightValue));
        return this;
    }

    @Override
    public final Conditions and(Expression leftField, String operator, Expression rightField) {
        and(leftField, operator(operator), rightField);
        return this;
    }

    @Override
    public final Conditions and(Expression leftField, Operator operator, Expression rightField) {
        and(condition(leftField, operator, rightField));
        return this;
    }

    @Override
    public final Conditions and(Conditions conditions) {
        CONDITIONS.add(new ConditionsImpl(AND, conditions));
        return this;
    }

    @Override
    public final Conditions andNot(String leftValue, String operator, String rightValue) {
        andNot(asConstant(leftValue), operator, asConstant(rightValue));
        return this;
    }

    @Override
    public final Conditions andNot(Expression leftField, String operator, Expression rightField) {
        andNot(leftField, operator(operator), rightField);
        return this;
    }

    @Override
    public final Conditions andNot(Expression leftField, Operator operator, Expression rightField) {
        andNot(condition(leftField, operator, rightField));
        return this;
    }

    @Override
    public final Conditions andNot(Conditions conditions) {
        CONDITIONS.add(new ConditionsImpl(AND_NOT, conditions));
        return this;
    }

    @Override
    public final Conditions or(String leftValue, String operator, String rightValue) {
        or(asConstant(leftValue), operator, asConstant(rightValue));
        return this;
    }

    @Override
    public final Conditions or(Expression leftField, String operator, Expression rightField) {
        or(leftField, operator(operator), rightField);

        return this;
    }

    @Override
    public final Conditions or(Expression leftField, Operator operator, Expression rightField) {
        or(condition(leftField, operator, rightField));

        return this;
    }

    @Override
    public final Conditions or(Conditions conditions) {
        CONDITIONS.add(new ConditionsImpl(OR, conditions));
        return this;
    }

    @Override
    public final Conditions orNot(String leftValue, String operator, String rightValue) {
        orNot(asConstant(leftValue), operator, asConstant(rightValue));
        return this;
    }

    @Override
    public final Conditions orNot(Expression leftField, String operator, Expression rightField) {
        orNot(leftField, operator(operator), rightField);
        return this;
    }

    @Override
    public final Conditions orNot(Expression leftField, Operator operator, Expression rightField) {
        orNot(condition(leftField, operator, rightField));
        return this;
    }

    @Override
    public final Conditions orNot(Conditions conditions) {
        CONDITIONS.add(new ConditionsImpl(OR_NOT, conditions));
        return this;
    }

    private boolean absentOperatorAndOperands() {
        return isEmpty(leftValue) && isEmpty(operator) && isEmpty(rightValue);
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        if (metadata != null)
            tree.startNode(metadata);
        else if (CONDITIONS.size() > 0)
            tree.startNode(EMPTY_GROUPED);
        else
            tree.startNode(EMPTY);

        if (!absentOperatorAndOperands())
            tree.startNode(new NodeMetadata(operator))
               .addLeaves(leftValue, rightValue)
               .endNode();

        if (CONDITIONS.size() == 1)
            tree.peek(CONDITIONS.get(0));
        else
            for (Conditions condition : CONDITIONS)
                tree.peek(condition);

        tree.endNode();
    }
}
