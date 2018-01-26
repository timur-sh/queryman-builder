/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.command.where.AndWhere;
import org.queryman.builder.command.where.OrWhere;
import org.queryman.builder.command.where.Where;

/**
 * It represents {@code WHERE} statement.
 *
 * @author Timur Shaidullin
 */
public final class WhereImpl implements
   Where,
   AndWhere,
   OrWhere {

    public static final String AND = "AND";
    public static final String OR  = "OR";

    String token;
    String leftValue;
    String rightValue;
    String operator;

    public WhereImpl(String leftValue, String operator, String rightValue) {
        this(null, leftValue, operator, rightValue);
    }

    public WhereImpl(String token, String leftValue, String operator, String rightValue) {
        this.token = token;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.operator = operator;
    }

    public String getToken() {
        return token;
    }

    public String getLeftValue() {
        return leftValue;
    }

    public String getRightValue() {
        return rightValue;
    }

    public String getOperator() {
        return operator;
    }
}
