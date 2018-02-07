/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.ast.NodeMetadata;
import org.queryman.builder.ast.NodesMetadata;
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
        return condition(asName(leftValue), operator(operator), asName(rightValue));
    }

    public static Conditions condition(Expression leftValue, String operator, Expression rightValue) {
        return condition(leftValue, operator(operator), rightValue);
    }

    public static Conditions condition(Expression leftValue, Operator operator, Expression rightValue) {
        return new ConditionsImpl(leftValue, new NodeMetadata(operator), rightValue);
    }

    /**
     * WHERE .. BETWEEN .. AND .. condition.
     */
    public static Conditions between(Expression field, Expression value1, Expression value2) {
        return new ConditionsImpl(NodesMetadata.BETWEEN, field, condition(value1, operator("AND"), value2));
    }

    /**
     * WHERE .. BETWEEN .. AND .. condition.
     */
    public static Conditions between(String field, String value1, String value2) {
        return between(asName(field), asName(value1), asName(value2));
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
