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

import java.util.LinkedList;
import java.util.List;

import static org.queryman.builder.PostgreSQL.condition;
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
    private String       leftValue;
    private String       operator;
    private String       rightValue;

    private final List<Conditions> CONDITIONS = new LinkedList<>();

    public ConditionsImpl(String leftValue, String operator, String rightValue) {
        this.leftValue = leftValue;
        this.operator = operator;
        this.rightValue = rightValue;
    }

    ConditionsImpl(NodeMetadata metadata, Conditions conditions) {
        CONDITIONS.add(conditions);
        this.metadata = metadata;
    }

    @Override
    public ConditionsImpl and(String leftValue, String operator, String rightValue) {
        CONDITIONS.add(new ConditionsImpl(AND, condition(leftValue, operator, rightValue)));
        return this;
    }

    @Override
    public ConditionsImpl andNot(String leftValue, String operator, String rightValue) {
        CONDITIONS.add(new ConditionsImpl(AND_NOT, condition(leftValue, operator, rightValue)));
        return this;
    }

    @Override
    public Conditions and(Conditions conditions) {
        CONDITIONS.add(new ConditionsImpl(AND, conditions));
        return this;
    }

    @Override
    public Conditions andNot(Conditions conditions) {
        CONDITIONS.add(new ConditionsImpl(AND_NOT, conditions));
        return this;
    }

    @Override
    public ConditionsImpl or(String leftValue, String operator, String rightValue) {
        CONDITIONS.add(new ConditionsImpl(OR, condition(leftValue, operator, rightValue)));
        return this;
    }

    @Override
    public ConditionsImpl orNot(String leftValue, String operator, String rightValue) {
        CONDITIONS.add(new ConditionsImpl(OR_NOT, condition(leftValue, operator, rightValue)));
        return this;
    }

    @Override
    public Conditions or(Conditions conditions) {
        CONDITIONS.add(new ConditionsImpl(OR, conditions));
        return this;
    }

    @Override
    public Conditions orNot(Conditions conditions) {
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
            tree.addLeaves(leftValue, operator, rightValue);

        if (CONDITIONS.size() == 1)
            tree.peek(CONDITIONS.get(0));
        else
            for (Conditions condition : CONDITIONS)
                tree.peek(condition);

        tree.endNode();
    }
}
