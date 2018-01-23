/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard implementation of {@link Node}.
 *
 * @author Timur Shaidullin
 */
public final class NodeImpl implements Node {
    private final String nodeName;
    private final List<String> leaves = new ArrayList<>();
    private       List<Node>   nodes   = new ArrayList<>();

    private char separator = ' ';

    NodeImpl(String value) {
        this.nodeName = value;
    }

    @Override
    public Node addChildNode(Node node) {
        nodes.add(node);
        return this;
    }

    @Override
    public Node addLeaf(String name) {
        leaves.add(name);
        return this;
    }

    @Override
    public String getNodeName() {
        return nodeName;
    }

    @Override
    public List<String> getLeaves() {
        return leaves;
    }

    @Override
    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public boolean isEmpty() {
        return nodes.size() == 0;
    }

    @Override
    public Node setSeparator(char c) {
        separator = c;
        return this;
    }

    @Override
    public char getSeparator() {
        return separator;
    }
}
