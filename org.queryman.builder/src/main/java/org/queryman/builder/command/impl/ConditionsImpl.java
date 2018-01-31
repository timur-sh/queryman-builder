/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.NodeMetadata;
import org.queryman.builder.ast.NodesMetadata;
import org.queryman.builder.command.Conditions;

import java.util.LinkedList;
import java.util.List;

import static org.queryman.builder.ast.NodesMetadata.AND;
import static org.queryman.builder.ast.NodesMetadata.AND_NOT;
import static org.queryman.builder.ast.NodesMetadata.OR;
import static org.queryman.builder.ast.NodesMetadata.OR_NOT;

/**
 * @author Timur Shaidullin
 */
public final class ConditionsImpl implements
   Conditions {

    private NodeMetadata metadata;
    private final List<Condition> CONDITIONS = new LinkedList<>();

    public ConditionsImpl(String leftValue, String operator, String rightValue) {
        CONDITIONS.add(new Condition(leftValue, operator, rightValue));
    }

    ConditionsImpl(NodeMetadata metadata, Conditions conditions) {
        this.metadata = metadata;

        CONDITIONS.addAll(((ConditionsImpl) conditions).CONDITIONS);
    }

    public NodeMetadata getMetadata() {
        return metadata;
    }

    @Override
    public ConditionsImpl and(String leftValue, String operator, String rightValue) {
        CONDITIONS.add(new Condition(AND, leftValue, operator, rightValue));
        return this;
    }

    @Override
    public ConditionsImpl andNot(String leftValue, String operator, String rightValue) {
        CONDITIONS.add(new Condition(AND_NOT, leftValue, operator, rightValue));
        return this;
    }

    @Override
    public ConditionsImpl or(String leftValue, String operator, String rightValue) {
        CONDITIONS.add(new Condition(OR, leftValue, operator, rightValue));
        return this;
    }

    @Override
    public ConditionsImpl orNot(String leftValue, String operator, String rightValue) {
        CONDITIONS.add(new Condition(OR_NOT, leftValue, operator, rightValue));
        return this;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        if (metadata != null)
            tree.startNode(metadata);

        if (CONDITIONS.size() == 1) {
            Condition condition1 = CONDITIONS.get(0);
            assemble1(tree, condition1);

        } else {
            tree.startNode(NodesMetadata.EMPTY_GROUPED);

            for (Condition condition : CONDITIONS) {
                if (condition.getMetadata() == null)
                    assemble1(tree, condition);
                else {
                    tree.startNode(condition.getMetadata());
                    assemble1(tree, condition);
                    tree.endNode();
                }
            }

            tree.endNode();
        }

        if (metadata != null)
            tree.endNode();
    }

    private void assemble1(AbstractSyntaxTree tree, Condition condition) {
        if (condition.getOperator() != null) {
            tree.startNode(new NodeMetadata(condition.getOperator(), 1))
               .addLeaves(condition.getLeftValue(), condition.getRightValue())
               .endNode();
        }
    }

    private class Condition {

        private NodeMetadata metadata;
        private String       leftValue;
        private String       rightValue;
        private String       operator;

        Condition(NodeMetadata metadata) {
            metadata = metadata;
        }

        Condition(String leftValue, String operator, String rightValue) {
            this(null, leftValue, operator, rightValue);
        }

        Condition(NodeMetadata token, String leftValue, String operator, String rightValue) {
            this.metadata = token;
            this.leftValue = leftValue;
            this.rightValue = rightValue;
            this.operator = operator;
        }

        public final NodeMetadata getMetadata() {
            return metadata;
        }

        public final String getLeftValue() {
            return leftValue;
        }

        public final String getRightValue() {
            return rightValue;
        }

        public final String getOperator() {
            return operator;
        }
    }

}
