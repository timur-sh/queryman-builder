/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.Metadata;
import org.queryman.builder.token.Token;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static org.queryman.builder.ast.NodeUtil.node;

/**
 * Standard implementation of {@link AbstractSyntaxTree}.
 *
 * If {@link #NODES} stack is empty, a new node is pushed at the top of it. Otherwise
 * the new node is added as a child of latest node of {@link #NODES} stack,
 * then the new node is pushed to this stack.
 *
 * @author Timur Shaidullin
 */
public class AbstractSyntaxTreeImpl implements AbstractSyntaxTree {
    private Node root;

    private       Stack<Node>   NODES     = new Stack<>();
    private final TreeFormatter FORMATTER = new TreeFormatter();

    public AbstractSyntaxTreeImpl(Metadata metadata) {

    }

    @Override
    public AbstractSyntaxTree startNode(NodeMetadata metadata) {
        startNode(metadata, " ");
        return this;
    }

    @Override
    public AbstractSyntaxTree startNode(NodeMetadata metadata, String delimiter) {
        Node node = node(metadata)
           .setDelimiter(delimiter);

        if (NODES.size() > 0) {
            NODES.peek().addChildNode(node);
        }

        NODES.push(node);

        return this;
    }

    @Override
    public AbstractSyntaxTree setDelimiter(String delimiter) {
        NODES.peek().setDelimiter(delimiter);
        return this;
    }

    @Override
    public AbstractSyntaxTree endNode() {
        if (NODES.size() == 1) {
            root = NODES.pop();
        } else {
            NODES.pop();
        }

        return this;
    }

    @Override
    public AbstractSyntaxTree addLeaf(Token token) {
        NODES.peek().addLeaf(token);

        return this;
    }

    @Override
    public AbstractSyntaxTree addLeaves(Token... tokens) {
        NODES.peek().getLeaves().addAll(Arrays.asList(tokens));

        return this;
    }

    @Override
    public AbstractSyntaxTree addLeaves(List<Token> tokens) {
        NODES.peek().getLeaves().addAll(tokens);

        return this;
    }

    @Override
    public AbstractSyntaxTree addChildNode(Node node) {
        NODES.peek().addChildNode(node);
        return this;
    }

    @Override
    public AbstractSyntaxTree reinitialize() {
        NODES.clear();
        return this;
    }

    @Override
    public AbstractSyntaxTree peek(AstVisitor node) {
        node.assemble(this);
        return this;
    }

    @Override
    public Node getRootNode() {
        if (root == null)
            throw new BrokenTreeException();

        return root;
    }

    @Override
    public String toString() {
        if (NODES.size() != 0) {
            throw new BrokenTreeException();
        }

        return FORMATTER.treeToString(root);
    }
}
