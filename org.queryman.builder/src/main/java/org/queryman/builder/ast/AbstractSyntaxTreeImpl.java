/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import java.util.Stack;

import static org.queryman.builder.ast.NodeUtil.*;

/**
 * Standard implementation of {@link AbstractSyntaxTree}.
 *
 * @author Timur Shaidullin
 */
public class AbstractSyntaxTreeImpl implements AbstractSyntaxTree {
    private Node root;

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

        //todo need concurrency tests.
        if (root == null) {
            root = node;
        } else {
            root.addChildNode(node);
        }

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
        return null;
    }


    @Override
    public AbstractSyntaxTree addLeaf(String node) {
        nodes.peek().addLeaf(node);

        return this;
    }

    @Override
    public AbstractSyntaxTree addChildNode(Node node) {
        nodes.peek().addChildNode(node);
        return this;
    }

    @Override
    public String toString() {
        return treeToString(root);
    }
}
