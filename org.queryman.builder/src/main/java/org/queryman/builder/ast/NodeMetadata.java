/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.token.Token;

/**
 * NodeMetadata contains configuration of particular node.
 *
 * @author Timur Shaidullin
 */
public class NodeMetadata {
    private final Token name;
    private boolean parentheses;

    // position in a condition, where operator is appeared
    private int position;

    public NodeMetadata(Token name) {
        this(name, 0, false);
    }

    public NodeMetadata(Token name, int position) {
        this(name, position, false);
    }

    public NodeMetadata(Token name, int position, boolean parentheses) {
        this.name = name;
        this.position = position;
        this.parentheses = parentheses;
    }

    public Token getToken() {
        return name;
    }

    public void setParentheses(boolean parentheses) {
        this.parentheses = parentheses;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isParentheses() {
        return parentheses;

    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return name.getName();
    }
}
