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
    private final Map<String, List<String>> tree  = new HashMap<>();
    private final Deque<Node>               nodes = new ArrayDeque<>();


    @Override
    public AbstractSyntaxTree startNode(Node node) {
//        nodes.add(node);
//        tree.put(node.getName(), new ArrayList<>());
        return this;
    }

    @Override
    public AbstractSyntaxTree endNode(Node node) {
        nodes.peekLast();
        return this;
    }

    @Override
    public AbstractSyntaxTree addLeaf(Leaf leaf) {
//        tree.get(nodes.getLast().getName()).add(leaf.toString());
        return this;
    }

    @Override
    public AbstractSyntaxTree addLeaf(String leaf) {
//        tree.get(nodes.getLast().getName()).add(leaf);
        return this;
    }

    @Override
    public AbstractSyntaxTree addChildNode(Leaf leaf) {
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
