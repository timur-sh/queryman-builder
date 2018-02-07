/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

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

/**
 * @author Timur Shaidullin
 */
public final class ConditionsImpl implements
   Conditions {

    private NodeMetadata metadata;
    private Token        leftValue;
    private Token        rightValue1;

    private final List<Conditions> CONDITIONS = new LinkedList<>();

    public ConditionsImpl(Expression leftValue, NodeMetadata metadata, Expression rightValue) {
        this.leftValue = leftValue;
        this.metadata = metadata;
        this.rightValue1 = rightValue;
    }

    public ConditionsImpl(NodeMetadata metadata, Expression field, Conditions conditions) {
        CONDITIONS.add(conditions);
        leftValue = field;
        this.metadata = metadata;
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

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        NodeMetadata metadata1 = metadata != null ? metadata : EMPTY;

        if (CONDITIONS.size() > 1 || hasNestedConditions(CONDITIONS))
            metadata1.setParentheses(true);

        tree.startNode(metadata1);

        if (leftValue != null)
            tree.addLeaf(leftValue);
        if (rightValue1 != null)
            tree.addLeaf(rightValue1);

        if (CONDITIONS.size() == 1)
            tree.peek(CONDITIONS.get(0));
        else
            for (Conditions condition : CONDITIONS)
                tree.peek(condition);

        tree.endNode();
    }

    private boolean hasNestedConditions(List<Conditions> conditions) {
        if (conditions.isEmpty())
            return false;

        if (!(conditions.get(0) instanceof ConditionsImpl))
            return false;

        ConditionsImpl condition = (ConditionsImpl) conditions.get(0);


        if (condition.CONDITIONS.size() > 0)
            return true;

        return false;
    }
}
