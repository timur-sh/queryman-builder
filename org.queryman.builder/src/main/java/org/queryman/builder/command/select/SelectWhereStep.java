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
public interface SelectWhereStep extends SelectGroupByStep {
    SelectWhereStep and(String left, String operator, String right);

    SelectWhereStep and(Expression left, Operator operator, Expression right);

    SelectWhereStep and(Expression field, Operator operator, Query query);

    SelectWhereStep and(Conditions conditions);

    SelectWhereStep andExists(Query query);


    SelectWhereStep andNot(String left, String operator, String right);

    SelectWhereStep andNot(Expression left, Operator operator, Expression right);

    SelectWhereStep andNot(Expression field, Operator operator, Query query);

    SelectWhereStep andNot(Conditions conditions);

    SelectWhereStep andNotExists(Query query);


    SelectWhereStep or(String left, String operator, String right);

    SelectWhereStep or(Expression left, Operator operator, Expression right);

    SelectWhereStep or(Expression field, Operator operator, Query query);

    SelectWhereStep or(Conditions conditions);

    SelectWhereStep orExists(Query query);


    SelectWhereStep orNot(String left, String operator, String right);

    SelectWhereStep orNot(Expression left, Operator operator, Expression right);

    SelectWhereStep orNot(Expression field, Operator operator, Query query);

    SelectWhereStep orNot(Conditions conditions);

    SelectWhereStep orNotExists(Query query);
}
