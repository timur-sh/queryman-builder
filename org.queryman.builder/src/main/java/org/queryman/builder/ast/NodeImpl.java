/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Standard implementation of {@link Node}.
 *
 * @author Timur Shaidullin
 */
public final class NodeImpl implements Node {
    private final String value;
    private Deque<Node> node = new ArrayDeque<>();

    NodeImpl(String value) {
        this.value = value;
    }

    @Override
    public Node addNode(Node node) {
        this.node.add(node);
        return this;
    }

    @Override
    public Node createNode(String value) {
        return new NodeImpl(value);
    }

    @Override
    public String getValue() {
        return value + " ";
    }

    private static String build(Deque<Node> node) {
        String val = "";

        if (node.isEmpty()) {
            return val;
        } else {
            val += node.pollFirst().getValue();
            return val + build(node);
        }
    }

    @Override
    public String toString() {
        return getValue() + build(this.node);
    }
}
