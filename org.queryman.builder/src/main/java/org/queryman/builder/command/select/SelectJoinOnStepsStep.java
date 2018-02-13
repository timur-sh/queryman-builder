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
public interface SelectJoinOnStepsStep extends SelectJoinManyStepsStep {
    SelectJoinOnStepsStep and(String left, String operator, String right);

    SelectJoinOnStepsStep and(Expression left, Operator operator, Expression right);

    SelectJoinOnStepsStep and(Expression field, Operator operator, Query query);

    SelectJoinOnStepsStep and(Conditions conditions);

    SelectJoinOnStepsStep andExists(Query query);


    SelectJoinOnStepsStep andNot(String left, String operator, String right);

    SelectJoinOnStepsStep andNot(Expression left, Operator operator, Expression right);

    SelectJoinOnStepsStep andNot(Expression field, Operator operator, Query query);

    SelectJoinOnStepsStep andNot(Conditions conditions);

    SelectJoinOnStepsStep andNotExists(Query query);


    SelectJoinOnStepsStep or(String left, String operator, String right);

    SelectJoinOnStepsStep or(Expression left, Operator operator, Expression right);

    SelectJoinOnStepsStep or(Expression field, Operator operator, Query query);

    SelectJoinOnStepsStep or(Conditions conditions);

    SelectJoinOnStepsStep orExists(Query query);


    SelectJoinOnStepsStep orNot(String left, String operator, String right);

    SelectJoinOnStepsStep orNot(Expression left, Operator operator, Expression right);

    SelectJoinOnStepsStep orNot(Expression field, Operator operator, Query query);

    SelectJoinOnStepsStep orNot(Conditions conditions);

    SelectJoinOnStepsStep orNotExists(Query query);
}
