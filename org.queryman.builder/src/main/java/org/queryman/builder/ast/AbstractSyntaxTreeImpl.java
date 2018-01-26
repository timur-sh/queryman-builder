/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static org.queryman.builder.ast.AstUtil.node;
import static org.queryman.builder.ast.AstUtil.treeToString;

/**
 * Standard implementation of {@link AbstractSyntaxTree}.
 *
 * @author Timur Shaidullin
 */
public class AbstractSyntaxTreeImpl implements AbstractSyntaxTree {
    private final Node root = new NodeImpl();

    private final Stack<Node> nodes = new Stack<>();

    @Override
    public AbstractSyntaxTree startNode(String name) {
        startNode(name, " ");
        return this;
    }

    @Override
    public AbstractSyntaxTree startNode(String name, String delimiter) {
        Node node = node(name);
        node.setDelimiter(delimiter);

        firstNode().addChildNode(node);

        nodes.push(node);
        return this;
    }

    @Override
    public AbstractSyntaxTree setDelimiter(String delimiter) {
        nodes.peek().setDelimiter(delimiter);
        return this;
    }

    @Override
    public AbstractSyntaxTree endNode() {
        nodes.pop();

        return this;
    }


    @Override
    public AbstractSyntaxTree addLeaf(String leaf) {
        nodes.peek().addLeaf(leaf);

        return this;
    }

    @Override
    public AbstractSyntaxTree addLeaves(String... leaves) {
        nodes.peek().getLeaves().addAll(Arrays.asList(leaves));

        return this;
    }

    @Override
    public AbstractSyntaxTree addLeaves(List<String> leaves) {
        nodes.peek().getLeaves().addAll(leaves);

        return this;
    }

    @Override
    public AbstractSyntaxTree addChildNode(Node node) {
        nodes.peek().addChildNode(node);
        return this;
    }

    @Override
    public AbstractSyntaxTree reinitialize() {
        root.clear();
        nodes.clear();
        return this;
    }

    @Override
    public AbstractSyntaxTree peek(ASTBuilder node) {
        node.assemble(this);
        return this;
    }

    @Override
    public String toString() {
        return treeToString(firstNode());
    }

    private Node firstNode() {
        if (root.isEmpty()) {
            return root;
        }

        return root.getNodes().get(0);
    }
}
