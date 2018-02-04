/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.Query;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

/**
 * @author Timur Shaidullin
 */
public interface SelectWhereFirstStep extends Query {
    SelectWhereStep where(String left, String operator, String right);

    SelectWhereStep where(Expression left, String operator, Expression right);

    SelectWhereStep where(Expression left, Operator operator, Expression right);

    SelectWhereStep where(Conditions conditions);
}