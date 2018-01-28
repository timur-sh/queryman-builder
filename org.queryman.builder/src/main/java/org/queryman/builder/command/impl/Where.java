/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.ast.NodeMetadata;

/**
 * @author Timur Shaidullin
 */
final class Where {

    NodeMetadata token;
    String leftValue;
    String rightValue;
    String operator;

    Where(NodeMetadata metadata) {
        token = metadata;
    }

    Where(String leftValue, String operator, String rightValue) {
        this(null, leftValue, operator, rightValue);
    }

    Where(NodeMetadata token, String leftValue, String operator, String rightValue) {
        this.token = token;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.operator = operator;
    }

    public final NodeMetadata getToken() {
        return token;
    }

    public final String getLeftValue() {
        return leftValue;
    }

    public final String getRightValue() {
        return rightValue;
    }

    public final String getOperator() {
        return operator;
    }
}
