/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Standard implementation of {@link AbstractSyntaxTree}.
 *
 * @author Timur Shaidullin
 */
public class AbstractSyntaxTreeImpl implements AbstractSyntaxTree {
    private Node root = ;

    private final Deque<Node> nodes = new ArrayDeque<>();

    @Override
    public AbstractSyntaxTree startNode(String node) {
        if (root == null) {
            root = new NodeImpl(node);
        }
        root.addNode(node);
        nodes.add(node);

        return this;
    }

    @Override
    public AbstractSyntaxTree endNode(Node node) {
        nodes.removeLast();
        return this;
    }

    @Override
    public AbstractSyntaxTree addLeaf(String node) {
        nodes.getLast().addNode(node);

        return this;
    }

    @Override
    public AbstractSyntaxTree addChildNode(Node node) {
        nodes.getLast().addNode(node);
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
