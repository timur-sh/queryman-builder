/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.Node;
import org.queryman.builder.ast.NodeImpl;
import org.queryman.builder.ast.NodeMetadata;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.condition;
import static org.queryman.builder.PostgreSQL.operator;
import static org.queryman.builder.ast.NodesMetadata.AND;
import static org.queryman.builder.ast.NodesMetadata.AND_NOT;
import static org.queryman.builder.ast.NodesMetadata.OR;
import static org.queryman.builder.ast.NodesMetadata.OR_NOT;

/**
 * @author Timur Shaidullin
 */
public final class ConditionsImpl implements
   Conditions {

    private Node node;

    public ConditionsImpl(Expression leftValue, NodeMetadata metadata, Expression rightValue) {
        node = new NodeImpl(metadata)
           .addLeaf(leftValue)
           .addLeaf(rightValue);
    }

    /**
     * Ordinarily this constructor is used by <code>BETWEEN .. AND ..</code>
     * clause.
     * <p>
     * Where:
     *
     * @param metadata   - {@code BETWEEN}
     * @param field      - field of between. Example: <code>id BETWEEN ..</code>
     * @param conditions - condition of between. Example: <code>id BETWEEN 1 AND 20</code>
     */
    public ConditionsImpl(NodeMetadata metadata, Expression field, Conditions conditions) {
        node = new NodeImpl(metadata)
           .addLeaf(field)
           .addChildNode(conditions.getNode());
    }

    ConditionsImpl(Conditions conditions) {
        node = rebuildNode(conditions);
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
        rebuildNode(AND, conditions);
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
        rebuildNode(AND_NOT, conditions);
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
        rebuildNode(OR, conditions);
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
        rebuildNode(OR_NOT, conditions);
        return this;
    }

    private void rebuildNode(NodeMetadata metadata, Conditions conditions) {
        node = new NodeImpl(metadata)
           .addChildNode(node)
           .addChildNode(rebuildNode(conditions));
    }

    private Node rebuildNode(Conditions conditions) {
        Node nodeAdjustment = conditions.getNode();

        if (nodeAdjustment.count() == 2) {
            NodeMetadata nodeMetadata = nodeAdjustment.getNodeMetadata().setParentheses(true);
            nodeAdjustment.setNodeMetadata(nodeMetadata);
        }

        return nodeAdjustment;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        Node node2;

        try {
            node2 = ((NodeImpl) node).clone();

            if (node2.count() == 2) {
                NodeMetadata nodeMetadata = node2.getNodeMetadata().setParentheses(true);
                node2.setNodeMetadata(nodeMetadata);
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        tree.addChildNode(node);
    }
}
