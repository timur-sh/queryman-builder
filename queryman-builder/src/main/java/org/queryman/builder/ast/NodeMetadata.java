/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.token.Operator;
import org.queryman.builder.token.Token;

/**
 * Immutable object.
 *
 * NodeMetadata contains configuration of particular node.
 *
 * @author Timur Shaidullin
 */
public class NodeMetadata {
    private final Token token;
    private boolean parentheses = false;
    private boolean joinNodes;

    // position in a condition, where operator is appeared
    private int position = 0;

    public NodeMetadata(Token token) {
        this.token = token;
        if (token instanceof Operator)
            position = ((Operator)token).getPosition();
    }

    public NodeMetadata(Token token, int position) {
        this.token = token;
        this.position = position;
    }

    public NodeMetadata(Token token, int position, boolean parentheses) {
        this.token = token;
        this.position = position;
        this.parentheses = parentheses;
    }

    public Token getToken() {
        return token;
    }

    public NodeMetadata setParentheses(boolean parentheses) {
        return new NodeMetadata(token, position, parentheses);
    }

    public NodeMetadata setPosition(int position) {
        return new NodeMetadata(token, position, parentheses);
    }

    public boolean isParentheses() {
        return parentheses;

    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return token.getName();
    }

    public boolean isJoinNodes() {
        return joinNodes;
    }

    public NodeMetadata setJoinNodes(boolean joinNodes) {
        this.joinNodes = joinNodes;
        return this;
    }
}
