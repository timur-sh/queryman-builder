/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.command.impl.ConditionsImpl;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.token.Constant;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Keyword;
import org.queryman.builder.token.Name;
import org.queryman.builder.token.Operator;
import org.queryman.builder.token.QualifiedName;
import org.queryman.builder.token.UnqualifiedName;

/**
 * @author Timur Shaidullin
 */
public class PostgreSQL {
    //----
    // OPERATORS
    //----

    public static Operator operator(String operator) {
        return new Operator(operator);
    }

    public static Conditions condition(String leftValue, String operator, String rightValue) {
        return new ConditionsImpl(leftValue, operator, rightValue);
    }

    public static Keyword keyword(String keyword) {
        return new Keyword(keyword);
    }

    public static Expression expression(String constant) {
        return new Expression(constant);
    }

    public static Name qualifiedName(String name) {
        return new QualifiedName(name);
    }

    public static Name unqualifiedName(String name) {
        return new UnqualifiedName(name);
    }

    //----
    // CONSTANTS
    //----

    public static Constant constant(String constant) {
        return new Constant(constant);
    }

    public static Constant asString(String constant) {
        return new Constant(constant);
    }

    public static Constant asNumber(Number constant) {
        return new Constant(constant.toString());
    }
}
