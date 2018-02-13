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
 * Compulsory step for all JOIN clauses. Exclusion is CROSS JOIN
 *
 * @see SelectJoinStep
 * @author Timur Shaidullin
 */
public interface SelectJoinOnStep extends Query {
    SelectJoinManyStepsStep on(boolean all);

    SelectJoinOnStepsStep on(String left, String operator, String right);

    SelectJoinOnStepsStep on(Expression left, Operator operator, Expression right);

    SelectJoinOnStepsStep on(Expression field, Operator operator, Query query);

    SelectJoinOnStepsStep on(Conditions conditions);

    SelectJoinOnStepsStep onExists(Query query);

    SelectJoinManyStepsStep using(String... columns);

    SelectJoinManyStepsStep using(Expression... columns);



}
