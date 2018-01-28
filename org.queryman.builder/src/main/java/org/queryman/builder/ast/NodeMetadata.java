/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * @author Timur Shaidullin
 */
public class NodeMetadata {
    private final String name;
    private boolean parentheses;

    public NodeMetadata(String name) {
        this.name = name;
    }

    public boolean hasParentheses() {
        return parentheses;
    }

    public NodeMetadata setParentheses(boolean parentheses) {
        this.parentheses = parentheses;
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
