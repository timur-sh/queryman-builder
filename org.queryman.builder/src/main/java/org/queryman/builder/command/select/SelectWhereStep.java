/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.command.Conditions;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

/**
 * @author Timur Shaidullin
 */
public interface SelectWhereStep extends SelectWhereManySteps {
    SelectWhereStep and(String left, String operator, String right);

    SelectWhereStep and(Expression left, String operator, Expression right);

    SelectWhereStep and(Expression left, Operator operator, Expression right);

    SelectWhereStep and(Conditions conditions);

    SelectWhereStep andNot(String left, String operator, String right);

    SelectWhereStep andNot(Expression left, String operator, Expression right);

    SelectWhereStep andNot(Expression left, Operator operator, Expression right);

    SelectWhereStep andNot(Conditions conditions);

    SelectWhereStep or(String left, String operator, String right);

    SelectWhereStep or(Expression left, String operator, Expression right);

    SelectWhereStep or(Expression left, Operator operator, Expression right);

    SelectWhereStep or(Conditions conditions);

    SelectWhereStep orNot(Conditions conditions);

    SelectWhereStep orNot(String left, String operator, String right);

    SelectWhereStep orNot(Expression left, String operator, Expression right);

    SelectWhereStep orNot(Expression left, Operator operator, Expression right);
}
