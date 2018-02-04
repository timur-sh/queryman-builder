/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.command.Conditions;
import org.queryman.builder.command.impl.ConditionsImpl;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Keyword;
import org.queryman.builder.token.Operator;

import static org.queryman.builder.token.Expression.ExpressionType.COLUMN_REFERENCE;
import static org.queryman.builder.token.Expression.ExpressionType.DEFAULT;
import static org.queryman.builder.token.Expression.ExpressionType.DOLLAR_STRING;
import static org.queryman.builder.token.Expression.ExpressionType.STRING_CONSTANT;

/**
 * @author Timur Shaidullin
 */
public class PostgreSQL {
    public static Operator operator(String operator) {
        return new Operator(operator);
    }

    public static Conditions condition(String leftValue, String operator, String rightValue) {
        return new ConditionsImpl(leftValue, operator, rightValue);
    }

    public static Keyword keyword(String keyword) {
        return new Keyword(keyword);
    }

    //----
    // EXPRESSIONS
    //----

    public static Expression asConstant(String constant) {
        return new Expression(constant, DEFAULT);
    }

    public static Expression asString(String constant) {
        return new Expression(constant, STRING_CONSTANT);
    }

    public static Expression asDollarString(String constant) {
        return new Expression(constant, DOLLAR_STRING);
    }

    public static Expression asDollarString(String constant, String tagName) {
        return new Expression(constant, DOLLAR_STRING).setTagName(tagName);
    }

    public static Expression asNumber(Number constant) {
        return new Expression(constant, DEFAULT);
    }

    public static Expression asName(String constant) {
        return new Expression(constant, COLUMN_REFERENCE);
    }

    public static Expression asQuotedName(String constant) {
        return new Expression(constant, COLUMN_REFERENCE).setQuoted(true);
    }

    public static Expression asQualifiedName(String constant) {
        return new Expression(constant, COLUMN_REFERENCE);
    }

    public static Expression asQuotedQualifiedName(String constant) {
        return new Expression(constant, COLUMN_REFERENCE).setQuoted(true);
    }
}
